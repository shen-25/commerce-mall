package com.zengshen.usercenter.service;

import com.zengshen.usercenter.model.vo.UsersVO;
import com.zengshen.usercenter.model.bo.RegisterBO;
import com.zengshen.usercenter.model.pojo.Users;

public interface UserService {
    Users login(String username, String password);

    Users selectByUsername(String username);

    Users createUser(RegisterBO registerBO);

     UsersVO createRedisToken(Users users);

    public void logout(String userId);
}
