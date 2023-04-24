package com.zengshen.order.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 表名：order_status
 * 表注释：订单状态表;订单的每个状态更改都需要进行记录
 *         10：待付款  20：已付款，待发货  30：已发货，待收货（7天自动确认）  40：交易成功（此时可以评价）50：交易关闭（待付款时，用户取消 或 长时间未付款，系统识别后自动关闭）
 *         退货/退货，此分支流程不做，所以不加入
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_status")
public class OrderStatus {
    /**
     * 订单ID;对应订单表的主键id
     */
    @Id
    @Column(name = "order_id")
    private String orderId;

    /**
     * 订单状态
     */
    @Column(name = "order_status")
    private Integer orderStatus;

    /**
     * 订单创建时间;对应[10:待付款]状态
     */
    @Column(name = "created_time")
    private Date createdTime;

    /**
     * 支付成功时间;对应[20:已付款，待发货]状态
     */
    @Column(name = "pay_time")
    private Date payTime;

    /**
     * 发货时间;对应[30：已发货，待收货]状态
     */
    @Column(name = "deliver_time")
    private Date deliverTime;

    /**
     * 交易成功时间;对应[40：交易成功]状态
     */
    @Column(name = "success_time")
    private Date successTime;

    /**
     * 交易关闭时间;对应[50：交易关闭]状态
     */
    @Column(name = "close_time")
    private Date closeTime;

    /**
     * 留言时间;用户在交易成功后的留言时间
     */
    @Column(name = "comment_time")
    private Date commentTime;
}