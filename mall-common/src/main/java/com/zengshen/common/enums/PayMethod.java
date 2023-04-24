package com.zengshen.common.enums;

/**
 * @author word
 */

public enum PayMethod {

    /**
     * 支付方式
     */
    WEIXIN(1, "微信"),
    ALIPAY(2, "支付宝");

    public final Integer type;
    public final String value;

    PayMethod(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}