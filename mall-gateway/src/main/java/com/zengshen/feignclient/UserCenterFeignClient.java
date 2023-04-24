package com.zengshen.feignclient;

import com.zengshen.common.ApiRestResponse;
import com.zengshen.common.bo.LoginBO;
import com.zengshen.common.bo.RegisterBO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-center", path = "/passport")
public interface UserCenterFeignClient {

    @PostMapping("/login")
    ApiRestResponse login(@RequestBody LoginBO loginBO);

    @PostMapping("/register")
    public ApiRestResponse register(@RequestBody RegisterBO registerBO);
}
