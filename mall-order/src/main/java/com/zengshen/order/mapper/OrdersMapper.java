package com.zengshen.order.mapper;

import com.zengshen.order.model.pojo.Orders;
import com.zengshen.order.model.vo.MyOrdersVO;
import com.zengshen.order.model.vo.MySubOrderItemVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

@Repository
public interface OrdersMapper extends Mapper<Orders> {

    public List<MyOrdersVO> queryMyOrders(@Param("paramsMap") Map<String, Object> map);

    public List<MySubOrderItemVO> getSubItems(String orderId);

}