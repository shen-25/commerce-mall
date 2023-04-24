package com.zengshen.common.enums;

public enum BusinessExceptionEnum {

    /**
     * 枚举类型
     */

    SUCCESS(200 , "SUCCESS"),
    FAILED(500, "FAILED"),
    FALL_BACK(600,"接口出现未知异常"),
    BLOCK_ERROR(601, "限流或者降级了"),

    // 50x


    // 用户 相关错误 60x
    USER_NOT_EXIST(500, "用户不存在"),
    USER_IS_EXIST(500, "用户已存在"),
    PASSWORD_DOES_NOT_MATCH(500, "两次密码不一致"),
    PASSWORD_ERROR(500, "密码不正确");

    // 业务码 0 代表成功
    private final int code;

    // 响应消息
    private final String msg;

    BusinessExceptionEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public Integer getCode() {
        return code;
    }
}
