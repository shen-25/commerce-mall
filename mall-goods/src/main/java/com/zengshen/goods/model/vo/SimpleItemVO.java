package com.zengshen.goods.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 6个最新商品的简单数据类型
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleItemVO {

    private String itemId;
    private String itemName;
    private String itemUrl;
}
