package com.zengshen.usercenter.model.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class AddressBO {
    private String addressId;

    @NotBlank(message = "用户id不能为空")
    private String userId;

    @NotBlank(message = "收件人不能为空")
    private String receiver;
    @NotBlank(message = "电话号码不能为空")
    private String mobile;

    @NotBlank(message = "地址不能为空")
    private String province;

    @NotBlank(message = "城市不能为空")
    private String city;

    @NotBlank(message = "地区不能为空")
    private String district;

    private String detail;
}
