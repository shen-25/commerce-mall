package com.zengshen.goods.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class CategoryVO {
    private Integer id;
    private String name;
    private String type;
    private Integer fatherId;

    // 三级分类vo list
    private List<com.zengshen.goods.model.vo.SubCategoryVO> subCatList;
}
