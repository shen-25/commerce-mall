package com.zengshen.usercenter.model.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterBO {
    @NotBlank(message = "用户名不能为空")
    @Length(min = 1, max = 6)
    private String username;

    @NotBlank(message = "密码不能为空")
    @Length(min = 1, max = 6)

    private String password;

    @NotBlank(message = "确认密码不能为空")
    @Length(min = 1, max = 6)
    private String confirmPassword;
}
