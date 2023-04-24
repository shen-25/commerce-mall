package com.zengshen.usercenter.controller;

import com.zengshen.common.ApiRestResponse;
import com.zengshen.common.enums.BusinessExceptionEnum;
import com.zengshen.common.utils.JsonUtil;
import com.zengshen.mvc.utils.CookieUtil;
import com.zengshen.usercenter.model.vo.UsersVO;
import com.zengshen.usercenter.model.bo.LoginBO;
import com.zengshen.usercenter.model.bo.RegisterBO;
import com.zengshen.usercenter.model.pojo.Users;
import com.zengshen.usercenter.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("passport")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private  HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;


    @PostMapping("/login")
    public ApiRestResponse login(@RequestBody LoginBO userBO) {

        Users user = userService.login(userBO.getUsername(), userBO.getPassword());

        UsersVO usersVO = userService.createRedisToken(user);

        CookieUtil.setCookie(request, response, "user",
                JsonUtil.objectToString(usersVO), true);

        return ApiRestResponse.success(usersVO);
    }


    @PostMapping("/logout")
    public ApiRestResponse logout(@RequestParam String userId) {
        // 清除用户的相关信息的cookie
        CookieUtil.deleteCookie(request, response, "user");

        // TODO 用户退出登录，需要清空购物车
        // TODO 分布式会话中需要清除用户数据
        userService.logout(userId);
        return ApiRestResponse.success();
    }

    @PostMapping("/register")
    public ApiRestResponse register(@RequestBody RegisterBO registerBO) {
        if (!registerBO.getPassword().equals(registerBO.getConfirmPassword())) {
            return ApiRestResponse.errorEnum(BusinessExceptionEnum.PASSWORD_DOES_NOT_MATCH);
        }

        Users user = userService.createUser(registerBO);

        CookieUtil.setCookie(request, response, "user",
                JsonUtil.objectToString(user), true);

        return ApiRestResponse.success(user);

    }
}
