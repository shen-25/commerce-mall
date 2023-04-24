package com.zengshen.order.task;

import com.alibaba.fastjson.JSON;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.dataflow.DataflowJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.zengshen.order.model.pojo.OrderStatus;
import com.zengshen.order.service.OrderService;
import com.zengshen.rabbit.task.annotation.ElasticJobConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Calendar;
import java.util.List;


/**
 * @author word
 */
@ElasticJobConfig(
        name = "cancelOrderFlow",
        cron = "0 0/30 * * * ?",
        description = "测试定时任务",
        shardingTotalCount = 1,
        overwrite = true,
        streamingProcess = true
)
@Component
@Slf4j
public class OrderAutoCancelJob implements DataflowJob<OrderStatus> {

    @Autowired
    private OrderService orderService;

    @Override
    public List<OrderStatus> fetchData(ShardingContext shardingContext) {
        // 分片总数
        int totalCount = shardingContext.getShardingTotalCount();
        Calendar now = Calendar.getInstance();
        now.add(Calendar.SECOND, -30);
        List<OrderStatus> needCancelOrder = orderService.getNeedCancelOrder(now.getTime());
        return needCancelOrder;
    }

    @Override
    public void processData(ShardingContext shardingContext, List<OrderStatus> list) {
        int shardingTotalCount = shardingContext.getShardingTotalCount();
        if (!CollectionUtils.isEmpty(list)) {
            for (OrderStatus orderStatus : list) {
                String orderId = orderStatus.getOrderId();
                if ( Long.parseLong(orderId) % shardingTotalCount == 0) {
                    orderService.cancelOrder(orderId);
                    log.info("取消了订单：{}", JSON.toJSONString(orderStatus));
                }
            }
        }
    }
}
