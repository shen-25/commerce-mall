package com.zengshen.sdk.user;

import lombok.Data;

/**
 *  用户封装ThreadLocal中传输的用户信息
 */
@Data
public class UsersInfoToCart {

    private String userId;   // 用户id
    private String userKey; // 关联购物车
    private Boolean tempUser = false;// 客户端是否需要存储cookie：user-key

}
