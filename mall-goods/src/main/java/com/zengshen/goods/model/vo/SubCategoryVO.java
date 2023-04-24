package com.zengshen.goods.model.vo;

import lombok.Data;

@Data
public class SubCategoryVO {
    private Integer subId;
    private String subName;
    private String subType;
    private Integer subFatherId;
}
