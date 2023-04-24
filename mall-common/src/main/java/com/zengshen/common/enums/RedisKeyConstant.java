package com.zengshen.common.enums;

import lombok.Data;
import lombok.Getter;

/**
 * redis 的前缀key描述
 * @author word
 */

@Getter
public enum RedisKeyConstant {
    /**
     * key desc
     */
    verify_code("verify_code:", "验证码"),

    cart_user("cart_user:", "购物车，优雅的实现"),
    cart_user_id("cart_user_id:", "购物车,不优雅的实现"),
    six_new_items("six_new_items:", "首页展示的六条数据"),
    user_token("user_token:", "用户token");

    private final String key;
    private final String desc;

    RedisKeyConstant(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }
}
