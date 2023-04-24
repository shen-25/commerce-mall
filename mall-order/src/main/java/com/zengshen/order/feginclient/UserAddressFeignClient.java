package com.zengshen.order.feginclient;

import com.zengshen.common.ApiRestResponse;
import com.zengshen.sdk.user.UserAddress;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author word
 */
@FeignClient(name = "user-center", path = "/user-center")
public interface UserAddressFeignClient {

    @GetMapping("/address/get")
    public ApiRestResponse<UserAddress> getUserAddress(@RequestParam String  userId,
                                                       @RequestParam String addressId);
}
