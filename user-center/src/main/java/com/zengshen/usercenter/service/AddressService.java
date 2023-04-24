package com.zengshen.usercenter.service;

import com.zengshen.usercenter.model.bo.AddressBO;
import com.zengshen.usercenter.model.pojo.UserAddress;

import java.util.List;

/**
 * 统一一下
 * 新增用create
 * 更新用update
 * 删除用delete
 * 查询用 get
 */
public interface AddressService {
    void addAddress(AddressBO addressBO);

    List<UserAddress> queryByUserId(String userId);

   void updateDefaultAddress(String userId, String addressId);

    void deleteAddress(String addressId);

    void updateUserAddress(AddressBO addressBO);

    UserAddress getUserAddress(String userId, String addressId);
}
