package com.zengshen.usercenter.service.impl;

import com.zengshen.common.enums.BusinessExceptionEnum;
import com.zengshen.common.enums.YesOrNo;
import com.zengshen.common.exception.BusinessException;
import com.zengshen.common.utils.IdWorker;
import com.zengshen.usercenter.model.bo.AddressBO;
import com.zengshen.usercenter.mapper.UserAddressMapper;
import com.zengshen.usercenter.model.pojo.UserAddress;
import com.zengshen.usercenter.service.AddressService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private IdWorker sid;

    @Autowired
    private UserAddressMapper userAddressMapper;


    @Override
    @Transactional(rollbackFor = BusinessException.class)
    public void addAddress(AddressBO addressBO) {
        // 1. 判断当前用户是否存在地址，如果没有，则新增为'默认地址'
        int isDefault = 0;
        List<UserAddress> addressList = this.queryByUserId(addressBO.getUserId());
        if (CollectionUtils.isEmpty(addressList)) {
            isDefault = 1;
        }
        String addressId = sid.nextIdString();

        // 2. 保存地址到数据库
        UserAddress newAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO, newAddress);

        newAddress.setId(addressId);
        newAddress.setIsDefault(isDefault);
        newAddress.setCreatedTime(new Date());
        newAddress.setUpdatedTime(new Date());

        userAddressMapper.insert(newAddress);
    }

    @Override
    public List<UserAddress> queryByUserId(String userId) {
        Example example = new Example(UserAddress.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        return userAddressMapper.selectByExample(example);
    }

    @Transactional
    @Override
    public void updateDefaultAddress(String userId, String addressId) {

        // 1. 查看要修改的是否为默认地址
        Example updateExample = new Example(UserAddress.class);
        Example.Criteria updateCriteria = updateExample.createCriteria();
        updateCriteria.andEqualTo("id", addressId);
        updateCriteria.andEqualTo("userId", userId);
        UserAddress userAddress = userAddressMapper.selectOneByExample(updateExample);

        if (userAddress.getIsDefault().equals(YesOrNo.YES.getType())) {
            // 已经为默认地址了
            BusinessException.display(BusinessExceptionEnum.FAILED);
        }

        // 2.把之前的默认地址改为非默认地址
        Example example = new Example(UserAddress.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        criteria.andEqualTo("isDefault", YesOrNo.YES.getType());
        List<UserAddress> userAddresses = userAddressMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(userAddresses) && userAddresses.size() > 1) {
            // 有多个默认地址，系统报错
            BusinessException.display(BusinessExceptionEnum.FAILED);

        }
        if (!CollectionUtils.isEmpty(userAddresses)) {
            UserAddress defaultAddress = userAddresses.get(0);
            defaultAddress.setIsDefault(YesOrNo.NO.getType());
            userAddressMapper.updateByPrimaryKeySelective(defaultAddress);
        }
        // 3. 修改默认地址
        userAddress.setIsDefault(YesOrNo.YES.getType());
        userAddressMapper.updateByPrimaryKeySelective(userAddress);
    }

    @Override
    public void deleteAddress(String addressId) {
        userAddressMapper.deleteByPrimaryKey(addressId);
    }

    @Override
    public void updateUserAddress(AddressBO addressBO) {
        UserAddress address = new UserAddress();
        BeanUtils.copyProperties(addressBO, address);
        address.setId(addressBO.getAddressId());
        address.setUpdatedTime(new Date());
        userAddressMapper.updateByPrimaryKeySelective(address);
    }

    @Override
    public UserAddress getUserAddress(String userId, String addressId) {

        UserAddress singleAddress = new UserAddress();
        singleAddress.setId(addressId);
        singleAddress.setUserId(userId);

        return userAddressMapper.selectOne(singleAddress);
    }


}
