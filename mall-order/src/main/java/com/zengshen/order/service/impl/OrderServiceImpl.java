package com.zengshen.order.service.impl;

import com.github.pagehelper.PageHelper;
import com.zengshen.common.enums.OrderStatusEnum;
import com.zengshen.common.enums.YesOrNo;
import com.zengshen.common.utils.IdWorker;
import com.zengshen.common.utils.PageInfoResult;
import com.zengshen.common.utils.PageUtil;
import com.zengshen.order.feginclient.GoodsFeignClient;
import com.zengshen.order.feginclient.UserAddressFeignClient;
import com.zengshen.order.mapper.OrderItemsMapper;
import com.zengshen.order.mapper.OrderStatusMapper;
import com.zengshen.order.mapper.OrdersMapper;
import com.zengshen.order.model.bo.SubmitOrderBO;
import com.zengshen.order.model.pojo.OrderItems;
import com.zengshen.order.model.pojo.OrderStatus;
import com.zengshen.order.model.pojo.Orders;
import com.zengshen.order.model.vo.MerchantOrdersVO;
import com.zengshen.order.model.vo.MyOrdersVO;
import com.zengshen.order.model.vo.OrderVO;
import com.zengshen.order.service.OrderService;
import com.zengshen.sdk.goods.Items;
import com.zengshen.sdk.goods.ItemsSpec;
import com.zengshen.sdk.user.UserAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OrderItemsMapper orderItemsMapper;

    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private UserAddressFeignClient userAddressFeignClient;

    @Autowired
    private GoodsFeignClient goodsFeignClient;


    /**
     * 创建订单: 这里会涉及到分布式事务
     * 创建订单会涉及到多个步骤和校验, 当不满足情况时直接抛出异常;
     * 1. 校验请求对象是否合法
     * 2. 创建订单
     * 3. 扣减商品库存
     * 5. 发送消息通知  RabbitMQ
     * */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public OrderVO addOrder(SubmitOrderBO submitOrderBO) {

        String userId = submitOrderBO.getUserId();
        String addressId = submitOrderBO.getAddressId();
        String itemSpecIds = submitOrderBO.getItemSpecIds();
        int payMethod = submitOrderBO.getPayMethod();
        String leftMsg = submitOrderBO.getLeftMsg();
        // 包邮费用设置为0
        int postAmount = 0;
        UserAddress address = userAddressFeignClient.getUserAddress(userId, addressId).getData();

        // 1. 新订单数据保存
        Orders newOrder = new Orders();
        String orderId = idWorker.nextIdString();
        newOrder.setId(orderId);
        newOrder.setUserId(userId);

        newOrder.setReceiverName(address.getReceiver());
        newOrder.setReceiverMobile(address.getMobile());
        newOrder.setReceiverAddress(address.getProvince() + " " + address.getCity() + " " + address.getDistrict() + " " + address.getDetail());

        newOrder.setPostAmount(postAmount);

        newOrder.setPayMethod(payMethod);
        newOrder.setLeftMsg(leftMsg);

        newOrder.setIsComment(YesOrNo.NO.type);
        newOrder.setIsDelete(YesOrNo.NO.type);
        newOrder.setCreatedTime(new Date());
        newOrder.setUpdatedTime(new Date());

        // 2. 循环根据itemSpecIds保存订单商品信息表
        String[] itemSpecIdArr = itemSpecIds.split(",");
        // 商品原价累计
        int totalAmount = 0;
        // 优惠后的实际支付价格累计
        int realPayAmount = 0;
        for (String itemSpecId : itemSpecIdArr) {
            // TODO 整合redis后，商品购买的数量重新从redis的购物车中获取
            int buyCounts = 1;
            // 2.1 根据规格id，查询规格的具体信息，主要获取价格
            ItemsSpec itemSpec = goodsFeignClient.getBySpecId(itemSpecId).getData();
            totalAmount += itemSpec.getPriceNormal() * buyCounts;
            realPayAmount += itemSpec.getPriceDiscount() * buyCounts;

            // 2.2 根据商品id，获得商品信息以及商品图片
            String itemId = itemSpec.getItemId();
            Items item = goodsFeignClient.getByItemId(itemId).getData();
            String imgUrl  = goodsFeignClient.itemMainImgById(itemId).getData();
            // 2.3 循环保存子订单数据到数据库
            String subOrderId = idWorker.nextIdString();
            OrderItems subOrderItem = new OrderItems();
            subOrderItem.setId(subOrderId);
            subOrderItem.setOrderId(orderId);
            subOrderItem.setItemId(itemId);
            subOrderItem.setItemName(item.getItemName());
            subOrderItem.setItemImg(imgUrl);
            subOrderItem.setBuyCounts(buyCounts);
            subOrderItem.setItemSpecId(itemSpecId);
            subOrderItem.setItemSpecName(itemSpec.getName());
            subOrderItem.setPrice(itemSpec.getPriceDiscount());
            orderItemsMapper.insert(subOrderItem);

            // 2.4 在用户提交订单以后，规格表中需要扣除库存
            goodsFeignClient.updateStock(itemSpecId, buyCounts);
        }
        // 3.保存订单
        newOrder.setTotalAmount(totalAmount);
        newOrder.setRealPayAmount(realPayAmount);
        ordersMapper.insert(newOrder);

        // 4.保存订单状态表
        OrderStatus waitPayOrderStatus = new OrderStatus();
        waitPayOrderStatus.setOrderId(orderId);
        waitPayOrderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        waitPayOrderStatus.setCreatedTime(new Date());
        orderStatusMapper.insert(waitPayOrderStatus);

        // 5. 构建商户订单，用于传给支付中心
        MerchantOrdersVO merchantOrdersVO = new MerchantOrdersVO();
        merchantOrdersVO.setMerchantOrderId(orderId);
        merchantOrdersVO.setMerchantUserId(userId);
        merchantOrdersVO.setAmount(realPayAmount + postAmount);
        merchantOrdersVO.setPayMethod(payMethod);

        // 6. 构建自定义订单vo
        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setMerchantOrdersVO(merchantOrdersVO);

        return orderVO;
    }

    @Override
    public PageInfoResult getMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize) {
        Map<String, Object> map = new HashMap<>();
        map.put("userId", userId);
        if (orderStatus != null) {
            map.put("orderStatus", orderStatus);
        }

        PageHelper.startPage(page, pageSize);

        List<MyOrdersVO> list = ordersMapper.queryMyOrders(map);
        return PageUtil.setPageInfoResult(list, page);
    }

    @Override
    public BigDecimal getPrice(String orderId) {
        Orders orders = ordersMapper.selectByPrimaryKey(orderId);
        int realPayAmount = orders.getRealPayAmount();
        return new BigDecimal(String.valueOf(realPayAmount / 100));
    }

    @Override
    public void cancelOrder(String orderId) {
        OrderStatus orderStatus = orderStatusMapper.selectByPrimaryKey(orderId);
        orderStatus.setCloseTime(new Date());
        orderStatus.setOrderStatus(OrderStatusEnum.CLOSE.type);
        orderStatusMapper.updateByPrimaryKey(orderStatus);
        Orders orders = ordersMapper.selectByPrimaryKey(orderId);
        orders.setIsDelete(YesOrNo.YES.getType());
        ordersMapper.updateByPrimaryKey(orders);
    }

    @Override
    public List<OrderStatus> getNeedCancelOrder(Date createTime) {
        return orderStatusMapper.getOrderStatus(OrderStatusEnum.WAIT_PAY.type, createTime);
    }
}
