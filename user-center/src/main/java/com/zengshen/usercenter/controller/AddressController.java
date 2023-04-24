package com.zengshen.usercenter.controller;

import com.zengshen.common.ApiRestResponse;
import com.zengshen.usercenter.model.bo.AddressBO;
import com.zengshen.usercenter.model.pojo.UserAddress;
import com.zengshen.usercenter.service.AddressService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("address")
public class AddressController {

    @Autowired
    private AddressService addressService;

    /**
     * 用户新增地址
     */
    @PostMapping("add")
    public ApiRestResponse add(@Valid @RequestBody AddressBO addressBO) {
        addressService.addAddress(addressBO);
        return ApiRestResponse.success();
    }

    /**
     * 查询单个地址
     */
    @GetMapping("/get")
    public ApiRestResponse<UserAddress> getUserAddress(@RequestParam String  userId,
                               @RequestParam String addressId) {
        UserAddress userAddress = addressService.getUserAddress(userId, addressId);
        return ApiRestResponse.success(userAddress);
    }

    @PostMapping("list")
    public ApiRestResponse list(@RequestParam String userId) {
        if (StringUtils.isBlank(userId)) {
            return ApiRestResponse.errorMsg("userId不能为空");
        }
        List<UserAddress> userAddresses = addressService.queryByUserId(userId);
        return ApiRestResponse.success(userAddresses);
    }

    @PostMapping("setDefalut")
    public ApiRestResponse setDefault(@RequestParam String userId, @RequestParam String addressId) {
        addressService.updateDefaultAddress(userId, addressId);
        return ApiRestResponse.success();
    }

    @PostMapping("delete")
    public ApiRestResponse delete(@RequestParam String userId, @RequestParam String addressId) {
        addressService.deleteAddress(addressId);
        return ApiRestResponse.success();
    }

    @PostMapping("/update")
    public ApiRestResponse update(@Valid @RequestBody AddressBO addressBO) {

        addressService.updateUserAddress(addressBO);

        return ApiRestResponse.success();
    }
}
