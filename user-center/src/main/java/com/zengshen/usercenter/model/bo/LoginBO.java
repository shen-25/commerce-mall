package com.zengshen.usercenter.model.bo;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author word
 */
@Data
public class LoginBO {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
}
