package com.zengshen.goods.mapper;

import com.zengshen.goods.model.vo.CategoryVO;
import com.zengshen.goods.model.pojo.Category;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@Repository
public interface CategoryMapper extends Mapper<Category> {

}