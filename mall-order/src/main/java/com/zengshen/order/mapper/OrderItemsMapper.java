package com.zengshen.order.mapper;

import com.zengshen.order.model.pojo.OrderItems;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface OrderItemsMapper extends Mapper<OrderItems> {
}