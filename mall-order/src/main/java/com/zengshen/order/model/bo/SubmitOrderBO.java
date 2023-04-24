package com.zengshen.order.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用于创建订单的BO对象
 * @author word
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmitOrderBO {

    private String userId;
    private String itemSpecIds;
    private String addressId;
    private Integer payMethod;
    private String leftMsg;
}