package com.zengshen.filter;

import com.alibaba.fastjson.JSONObject;
import com.zengshen.common.ApiRestResponse;
import com.zengshen.common.bo.LoginBO;
import com.zengshen.common.enums.RedisKeyConstant;
import com.zengshen.common.constant.HeaderKeyConstant;
import com.zengshen.feignclient.UserCenterFeignClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author word
 */
@Slf4j
@Component
public class GlobalLoginOrRegisterFilter implements GlobalFilter, Ordered {

    @Autowired
    private UserCenterFeignClient userCenterFeignClient;

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getURI().getPath();

        if (path.contains("mall-goods")) {
            return chain.filter(exchange);
        }
        if (path.contains("mall-cart")) {
            return chain.filter(exchange);
        }
        // 这个比较特殊
        if (path.contains("/orders/getPrice")) {
            return chain.filter(exchange);
        }
        path = path.substring(path.lastIndexOf("/"));
        // 1. 如果是登录
        if (path.contains(HeaderKeyConstant.LOGIN_URI)) {
            return chain.filter(exchange);
        }
        // 2. 如果是注册
        if (path.contains(HeaderKeyConstant.REGISTER_URI)) {
            return chain.filter(exchange);
        }
        // 2. 如果是退出
        if (path.contains(HeaderKeyConstant.LOGOUT_URI)) {
            return chain.filter(exchange);
        }

        // 3.访问其他的服务, 则鉴权
        HttpHeaders headers = request.getHeaders();

        String userId = headers.getFirst(HeaderKeyConstant.HEADER_USERID);
        String token = headers.getFirst(HeaderKeyConstant.HEADER_USERTOKEN);

        if (StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(token)) {
            String redisToken = redisTemplate.opsForValue().get(RedisKeyConstant.user_token.getKey() + userId);
            if (StringUtils.isNotBlank(redisToken) && redisToken.equals(token)) {
                return chain.filter(exchange);
            }
        }
        // 2.1 获取不到用户信息, 返回 401
        // 标记这次请求没有权限, 并结束这次请求
        JSONObject message = new JSONObject();
        message.put("status", -1);
        message.put("data", "鉴权失败");
        byte[] bits = message.toJSONString().getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = response.bufferFactory().wrap(bits);
        response.setStatusCode(HttpStatus.OK);
        //指定编码，否则在浏览器中会中文乱码
        response.getHeaders().add("Content-Type", "text/plain;charset=UTF-8");
        return response.writeWith(Mono.just(buffer));
    }


    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE + 1;
    }

}
