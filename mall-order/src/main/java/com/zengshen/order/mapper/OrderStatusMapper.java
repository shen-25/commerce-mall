package com.zengshen.order.mapper;

import com.zengshen.order.model.pojo.OrderStatus;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderStatusMapper extends Mapper<OrderStatus> {

    public List<OrderStatus> getOrderStatus(@Param("orderStatus") int orderStatus,
                                            @Param("createTime") Date createTime);
}