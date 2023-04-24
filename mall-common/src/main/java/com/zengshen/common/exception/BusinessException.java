package com.zengshen.common.exception;


import com.zengshen.common.enums.BusinessExceptionEnum;

/**
 * @author word
 */
public class BusinessException extends RuntimeException{

    private BusinessExceptionEnum businessExceptionEnum;

    public BusinessException(BusinessExceptionEnum businessExceptionEnum) {
        super("异常状态码为: " + businessExceptionEnum.getCode()
        + "; 异常信息为: " + businessExceptionEnum.getMsg());
        this.businessExceptionEnum = businessExceptionEnum;
    }

    public BusinessExceptionEnum getBusinessExceptionEnum() {
        return businessExceptionEnum;
    }

    public void setBusinessExceptionEnum(BusinessExceptionEnum businessExceptionEnum) {
        this.businessExceptionEnum = businessExceptionEnum;
    }

    /**
     * @author word
     * 统一的异常返回，统一封装
     */
    public static void display(BusinessExceptionEnum businessExceptionEnum) {
        throw new BusinessException(businessExceptionEnum);
    }
}
