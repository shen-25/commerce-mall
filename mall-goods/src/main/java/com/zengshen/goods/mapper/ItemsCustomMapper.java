package com.zengshen.goods.mapper;

import com.zengshen.goods.model.vo.SearchItemsVO;
import com.zengshen.goods.model.vo.ShopCartVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemsCustomMapper {

    List<SearchItemsVO> searchItemsByCategoryId(@Param("catId") String catId, @Param("sort") String sort);
    List<SearchItemsVO> searchItemsByKeyword(@Param("keyword") String keyword, @Param("sort") String sort);

    List<ShopCartVO> queryItemsBySpecIds(@Param("specIdList") List<String> specIdList);

    public int decreaseItemSpecStock(@Param("specId") String specId,
                                     @Param("pendingCounts") int pendingCounts);

}