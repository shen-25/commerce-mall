package com.zengshen.usercenter.service.impl;

import com.zengshen.common.enums.BusinessExceptionEnum;

import com.zengshen.common.exception.BusinessException;
import com.zengshen.usercenter.model.bo.CenterUserBO;
import com.zengshen.usercenter.mapper.UsersMapper;
import com.zengshen.usercenter.model.pojo.Users;
import com.zengshen.usercenter.service.CenterUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CenterUserServiceImpl implements CenterUserService {
    @Autowired
    private UsersMapper usersMapper;

    @Override
    public Users queryById(String userId) {
        Users users = usersMapper.selectByPrimaryKey(userId);
        users.setPassword(null);
        return users;
    }

    @Override
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO) {
        Users users = new Users();
        BeanUtils.copyProperties(centerUserBO, users);
        users.setId(userId);
        users.setUpdatedTime(new Date());
        usersMapper.updateByPrimaryKeySelective(users);
        return users;
    }


    @Override
    public Users updateUserFace(String userId, String faceUrl) {
        Users users = queryById(userId);

        if (users == null) {
            BusinessException.display(BusinessExceptionEnum.FAILED);
        }
        users.setFace(faceUrl.trim());
        usersMapper.updateByPrimaryKeySelective(users);
        return users;
    }
}
