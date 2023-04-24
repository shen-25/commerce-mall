package com.zengshen.cart.interceptor;

import com.zengshen.cart.constant.CartConstant;
import com.zengshen.common.constant.HeaderKeyConstant;
import com.zengshen.common.enums.RedisKeyConstant;
import com.zengshen.mvc.utils.CookieUtil;
import com.zengshen.sdk.user.UsersInfoToCart;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author word
 */
public class CartInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public static ThreadLocal<UsersInfoToCart> toThreadLocal = new ThreadLocal<>();

    /***
     * 拦截所有请求给ThreadLocal封装UsersVO对象
     * 目标方法执行之前：在ThreadLocal中存入用户信息【同一个线程共享数据】
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){
        UsersInfoToCart userInfoTo = new UsersInfoToCart();
        //获得当前登录用户的信息
        String userId = request.getHeader(HeaderKeyConstant.HEADER_USERID);
        String token = request.getHeader(HeaderKeyConstant.HEADER_USERTOKEN);
        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(token)) {
            String redisToken = redisTemplate.opsForValue().get(RedisKeyConstant.user_token.getKey() + userId);
            // 用户登录了
            if (StringUtils.isNotBlank(redisToken) && redisToken.equals(token)) {
                userInfoTo.setUserId(userId);
            }
        }
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                //user-key
                String name = cookie.getName();
                if (name.equals(CartConstant.TEMP_USER_COOKIE_NAME)) {
                    userInfoTo.setUserKey(cookie.getValue());
                    // 标识客户端已经存储了 user-key
                    userInfoTo.setTempUser(true);
                }
            }
        }
        //如果没有临时用户分配一个临时用户
        if (StringUtils.isBlank(userInfoTo.getUserKey())) {
            String uuid = UUID.randomUUID().toString();
            userInfoTo.setUserKey(uuid);
        }
        //目标方法执行之前
        toThreadLocal.set(userInfoTo);
        return true;
    }

    /**
     * 业务执行之后，让浏览器保存临时用户user-key
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        // 获取当前用户的值
        UsersInfoToCart userInfoTo = toThreadLocal.get();
        // 1、判断是否登录和判断是否创建 user-token 的 cookie
        if (userInfoTo != null && !userInfoTo.getTempUser()) {
            //创建一个cookie
            CookieUtil.setCookie(request, response,
                    CartConstant.TEMP_USER_COOKIE_NAME,
                    userInfoTo.getUserKey(), CartConstant.TEMP_USER_COOKIE_TIMEOUT);
        }
    }

}