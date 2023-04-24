package com.zengshen.usercenter.mapper;

import com.zengshen.usercenter.model.pojo.Users;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author word
 */
@Repository
public interface UsersMapper extends Mapper<Users> {
}