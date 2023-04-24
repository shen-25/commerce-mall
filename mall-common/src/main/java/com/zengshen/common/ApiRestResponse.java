package com.zengshen.common;

import com.zengshen.common.enums.BusinessExceptionEnum;
import com.zengshen.common.utils.JsonUtil;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;

/**
 * @author word
 */
@Data
public class ApiRestResponse<T> implements Serializable {

    // 响应状态码
    private int status;

    // 响应中的信息
    private String msg;

    // 响应的内容
    private T data;

    private ApiRestResponse(Integer code, String msg, T data) {
        this.status = code;
        this.msg = msg;
        this.data = data;
    }
    public ApiRestResponse(Integer code, String msg) {
        this.status = code;
        this.msg = msg;
    }

    private ApiRestResponse() {
        this(BusinessExceptionEnum.SUCCESS.getCode(),BusinessExceptionEnum.SUCCESS.getMsg());
    }

    private ApiRestResponse(T data) {
        this(BusinessExceptionEnum.SUCCESS.getCode(),BusinessExceptionEnum.SUCCESS.getMsg(), data);
    }

    private ApiRestResponse(BusinessExceptionEnum businessExceptionEnum) {
        this.status = businessExceptionEnum.getCode();
        this.msg = businessExceptionEnum.getMsg();
    }


    private ApiRestResponse(BusinessExceptionEnum businessExceptionEnum, String msg) {
        this.status = businessExceptionEnum.getCode();
        this.msg = msg;
    }
    private ApiRestResponse(BusinessExceptionEnum businessExceptionEnum, T data) {
        this.status = businessExceptionEnum.getCode();
        this.msg = businessExceptionEnum.getMsg();
        this.data = data;
    }

    public static ApiRestResponse<String> success() {
        return new ApiRestResponse<>();
    }

    public static<T>  ApiRestResponse<T> success(T result){
        return new ApiRestResponse<T>(result);
    }

    public static<T>  ApiRestResponse<T> errorException(BusinessExceptionEnum businessExceptionEnum) {
        return new ApiRestResponse<T>(businessExceptionEnum);
    }

    public static<T> ApiRestResponse<T> error() {
        return new ApiRestResponse<T>(BusinessExceptionEnum.FAILED);
    }

    public static<T> ApiRestResponse<T> errorEnum(BusinessExceptionEnum businessExceptionEnum) {
        return new ApiRestResponse<T>(businessExceptionEnum);
    }


    public static<T> ApiRestResponse<T> errorMsg(String msg) {
        return new ApiRestResponse<T>(BusinessExceptionEnum.FAILED, msg);
    }

    public static<T> ApiRestResponse<T> errorMap(T map) {
        return new ApiRestResponse<T>(BusinessExceptionEnum.FAILED, JsonUtil.objectToString(map));
    }
}
