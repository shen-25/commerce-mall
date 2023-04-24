package com.zengshen.goods.model.vo;


import com.zengshen.goods.model.pojo.Items;
import com.zengshen.goods.model.pojo.ItemsImg;
import com.zengshen.goods.model.pojo.ItemsParam;
import com.zengshen.goods.model.pojo.ItemsSpec;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemInfoVO {
    private Items item;
    private List<ItemsImg> itemImgList;
    private List<ItemsSpec> itemSpecList;
    private ItemsParam itemParams;

}
