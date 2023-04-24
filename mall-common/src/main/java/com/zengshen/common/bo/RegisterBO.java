package com.zengshen.common.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterBO {

    private String username;

    private String password;

    private String confirmPassword;
}
