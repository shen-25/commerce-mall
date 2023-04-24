package com.zengshen.order.service;

import com.zengshen.common.utils.PageInfoResult;
import com.zengshen.order.model.bo.SubmitOrderBO;
import com.zengshen.order.model.pojo.OrderStatus;
import com.zengshen.order.model.pojo.Orders;
import com.zengshen.order.model.vo.OrderVO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface OrderService {
    public OrderVO addOrder(SubmitOrderBO submitOrderBO);

    public PageInfoResult getMyOrders(String userId, Integer orderStatus, Integer page, Integer pageSize);


    BigDecimal getPrice(String orderId);

    public void cancelOrder(String orderId);

    public List<OrderStatus> getNeedCancelOrder(Date createTime);
}
