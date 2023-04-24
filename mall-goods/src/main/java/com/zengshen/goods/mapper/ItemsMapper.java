package com.zengshen.goods.mapper;

import com.zengshen.goods.model.pojo.Items;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

@Repository
public interface ItemsMapper extends Mapper<Items> {
}