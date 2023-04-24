package com.zengshen.goods.service;

import com.zengshen.common.utils.PageInfoResult;
import com.zengshen.goods.model.pojo.Items;
import com.zengshen.goods.model.pojo.ItemsImg;
import com.zengshen.goods.model.pojo.ItemsParam;
import com.zengshen.goods.model.pojo.ItemsSpec;
import com.zengshen.goods.model.vo.ShopCartVO;
import com.zengshen.sdk.goods.Goods;
import com.zengshen.sdk.goods.GoodsAttr;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;

public interface ItemsService {

    /**
     * 根据商品ID查询详情
     */
    public Items selectItemById(String itemId);

    /**
     * 根据商品id查询商品图片列表

     */
    public List<ItemsImg> selectItemImgList(String itemId);

    /**
     * 根据商品id查询商品规格

     */
    public List<ItemsSpec> selectItemSpecList(String itemId);

    /**
     * 根据商品id查询商品参数

     */
    public ItemsParam selectItemParam(String itemId);

    /**
     * 根据分类id和排序规则查找商品
     */
    PageInfoResult searchItemsByCategoryId(String catId, String sort, int page, int pageSize);

    PageInfoResult searchByKeyword(String keywords, String sort, int page, int pageSize);

    List<ShopCartVO> selectItemsBySpecIds(String itemSpecIds);

    ItemsSpec queryItemSpecById(String specId);

    String queryItemMainImgById(String itemId);

    void decreaseItemSpecStock(String itemSpecId, int buyCounts);

   BigDecimal getPrice(String itemId, String specId);

    Goods getGoods(String goodsId, String specId, String imgId);

    GoodsAttr getGoodsAttr(String goodsId, String specId);
}
