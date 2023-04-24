package com.zengshen.usercenter.service;

import com.zengshen.usercenter.model.bo.CenterUserBO;
import com.zengshen.usercenter.model.pojo.Users;

public interface CenterUserService {
    /**
     * 根据用户id查询用户信息
     * @return
     */
    public Users queryById(String userId);

    Users updateUserInfo(String userId, CenterUserBO centerUserBO);

    Users updateUserFace(String userId, String faceUrl);
}
