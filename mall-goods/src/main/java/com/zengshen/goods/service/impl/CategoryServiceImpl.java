package com.zengshen.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.zengshen.common.enums.RedisKeyConstant;
import com.zengshen.goods.mapper.CategoryCustomMapper;
import com.zengshen.goods.mapper.CategoryMapper;
import com.zengshen.goods.model.pojo.Category;
import com.zengshen.goods.model.vo.CategoryVO;
import com.zengshen.goods.model.vo.NewItemsVO;
import com.zengshen.goods.service.CategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryCustomMapper categoryCustomMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public List<Category> getRootCategoryList() {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type", 1);
        return categoryMapper.selectByExample(example);
    }

    @Override
    public List<CategoryVO> getSubCatList(String rootCatId) {
        return categoryCustomMapper.getSubCatList(rootCatId);
    }

    @Override
    public List<NewItemsVO> getSixNewItemsLazy(String rootCatId) {
        String rootCatIdKey = RedisKeyConstant.six_new_items.getKey() + rootCatId;
        String s = redisTemplate.opsForValue().get(rootCatIdKey);
        if (StringUtils.isBlank(s)) {
            List<NewItemsVO> sixNewItemsLazy = categoryCustomMapper.getSixNewItemsLazy(rootCatId);
            redisTemplate.opsForValue().set(rootCatIdKey, JSON.toJSONString(sixNewItemsLazy));
            return sixNewItemsLazy;
        }
        return JSON.parseArray(s, NewItemsVO.class);
    }
}
