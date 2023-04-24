package com.zengshen.usercenter.model.vo;

import lombok.Data;

/**
 * @author word
 */
@Data
public class UsersVO {

    private String id;

    private String username;

    private String nickname;

    private String face;

    private Integer sex;
    // 用户会话token
    private String userUniqueToken;
}
