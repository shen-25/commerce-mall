package com.zengshen.usercenter.service.impl;

import com.zengshen.common.enums.BusinessExceptionEnum;
import com.zengshen.common.enums.RedisKeyConstant;
import com.zengshen.common.enums.Sex;
import com.zengshen.common.exception.BusinessException;
import com.zengshen.common.utils.DateUtil;
import com.zengshen.common.utils.IdWorker;
import com.zengshen.common.utils.MD5Util;
import com.zengshen.usercenter.model.vo.UsersVO;
import com.zengshen.usercenter.mapper.UsersMapper;
import com.zengshen.usercenter.model.bo.RegisterBO;
import com.zengshen.usercenter.model.pojo.Users;
import com.zengshen.usercenter.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.UUID;

/**
 * @author word
 */
@Service
public class UserServiceImpl  implements UserService {

    @Autowired
    private UsersMapper usersMapper;

    @Autowired
    private IdWorker sid;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private static final String USER_FACE = "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png";

    @Override
    public Users login(String username, String password) {
        Users user = this.selectByUsername(username);
        if(user == null) {
            BusinessException.display(BusinessExceptionEnum.USER_NOT_EXIST);
        }
        if (!user.getPassword().equals(MD5Util.getMD5Str(password))) {
            BusinessException.display(BusinessExceptionEnum.PASSWORD_ERROR);
        }
        return user;
    }

    @Override
    public Users selectByUsername(String username) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        return usersMapper.selectOneByExample(example);
    }

    @Transactional
    @Override
    public Users createUser(RegisterBO registerBO) {
        Users tempUser = this.selectByUsername(registerBO.getUsername());
        if (tempUser != null) {
            BusinessException.display(BusinessExceptionEnum.USER_IS_EXIST);
        }

        Users user = new Users();
        user.setId(sid.nextIdString());
        user.setUsername(registerBO.getUsername());
        String md5Str = MD5Util.getMD5Str(registerBO.getPassword());
        user.setPassword(md5Str);
        // 默认用户昵称同用户名
        user.setNickname(registerBO.getUsername());
        // 默认头像
        user.setFace(USER_FACE);
        // 默认生日
        user.setBirthday(DateUtil.stringToDate("1900-01-01"));
        // 默认性别为 保密
        user.setSex(Sex.secret.getType());

        user.setCreatedTime(new Date());
        user.setUpdatedTime(new Date());
        usersMapper.insert(user);
        return user;
    }

    //
    @Override
    public UsersVO createRedisToken(Users users) {
        UsersVO usersVO = new UsersVO();
        UUID uuid = UUID.randomUUID();
        BeanUtils.copyProperties(users, usersVO);
        usersVO.setUserUniqueToken(uuid.toString());
        String key = RedisKeyConstant.user_token.getKey() + users.getId();
        redisTemplate.opsForValue().set(key, uuid.toString());
        return usersVO;
    }

    @Override
    public void logout(String userId) {
        String key = RedisKeyConstant.user_token.getKey() + userId;
        redisTemplate.delete(key);
    }
}
