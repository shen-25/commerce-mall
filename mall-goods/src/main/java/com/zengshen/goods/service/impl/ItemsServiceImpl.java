package com.zengshen.goods.service.impl;

import com.github.pagehelper.PageHelper;
import com.zengshen.common.enums.YesOrNo;
import com.zengshen.common.utils.PageInfoResult;
import com.zengshen.common.utils.PageUtil;
import com.zengshen.goods.mapper.*;
import com.zengshen.goods.model.pojo.Items;
import com.zengshen.goods.model.pojo.ItemsImg;
import com.zengshen.goods.model.pojo.ItemsParam;
import com.zengshen.goods.model.pojo.ItemsSpec;
import com.zengshen.goods.model.vo.SearchItemsVO;
import com.zengshen.goods.model.vo.ShopCartVO;
import com.zengshen.goods.service.ItemsService;
import com.zengshen.sdk.goods.Goods;
import com.zengshen.sdk.goods.GoodsAttr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItemsServiceImpl implements ItemsService {

    @Autowired
    private ItemsMapper itemsMapper;

    @Autowired
    private ItemsImgMapper itemsImgMapper;

    @Autowired
    private ItemsSpecMapper itemsSpecMapper;

    @Autowired
    private ItemsParamMapper itemsParamMapper;

    @Autowired
    private ItemsCustomMapper itemsCustomMapper;

    @Override
    public Items selectItemById(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }

    @Override
    public List<ItemsImg> selectItemImgList(String itemId) {
        Example example = new Example(ItemsImg.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsImgMapper.selectByExample(example);
    }

    @Override
    public List<ItemsSpec> selectItemSpecList(String itemId) {
        Example example = new Example(ItemsSpec.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsSpecMapper.selectByExample(example);
    }

    @Override
    public ItemsParam selectItemParam(String itemId) {
        Example example = new Example(ItemsParam.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        return itemsParamMapper.selectOneByExample(example);
    }

    @Override
    public PageInfoResult searchItemsByCategoryId(String catId, String sort, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<SearchItemsVO> searchItemsVOS = itemsCustomMapper.searchItemsByCategoryId(catId, sort);
        return PageUtil.setPageInfoResult(searchItemsVOS, page);
    }

    @Override
    public PageInfoResult searchByKeyword(String keywords, String sort, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        List<SearchItemsVO> keywordsList = itemsCustomMapper.searchItemsByKeyword(keywords, sort);
        return PageUtil.setPageInfoResult(keywordsList, page);
    }

    @Override
    public List<ShopCartVO> selectItemsBySpecIds(String itemSpecIds) {
        String ids[] = itemSpecIds.split(",");
        List<String> specIdsList = Arrays.stream(ids).collect(Collectors.toList());
        return itemsCustomMapper.queryItemsBySpecIds(specIdsList);
    }

    @Override
    public ItemsSpec queryItemSpecById(String specId) {
        return itemsSpecMapper.selectByPrimaryKey(specId);
    }

    @Override
    public String queryItemMainImgById(String itemId) {
        ItemsImg itemsImg = new ItemsImg();
        itemsImg.setItemId(itemId);
        itemsImg.setIsMain(YesOrNo.YES.type);
        ItemsImg result = itemsImgMapper.selectOne(itemsImg);
        return result != null ? result.getUrl() : "";
    }

    @Transactional
    @Override
    public void decreaseItemSpecStock(String itemSpecId, int buyCounts) {
       // 1. 查询库存
        int result = itemsCustomMapper.decreaseItemSpecStock(itemSpecId, buyCounts);
        if (result != 1) {
            throw new RuntimeException("订单创建失败，原因：库存不足!");
        }
    }

    @Override
    public BigDecimal getPrice(String itemId, String specId) {
        Example example = new Example(ItemsSpec.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", itemId);
        criteria.andEqualTo("id", specId);
        ItemsSpec itemsSpec = itemsSpecMapper.selectOneByExample(example);
        return new BigDecimal(String.valueOf(itemsSpec.getPriceDiscount() / 100));
    }

    @Override
    public Goods getGoods(String goodsId, String specId, String imgId) {
        ItemsSpec itemsSpec = itemsSpecMapper.selectByPrimaryKey(specId);
        ItemsImg itemsImg = itemsImgMapper.selectByPrimaryKey(imgId);
        Items items = itemsMapper.selectByPrimaryKey(goodsId);
        Goods goods = new Goods();
        goods.setId(items.getId());
        goods.setItemName(items.getItemName());
        goods.setOnOffStatus(goods.getOnOffStatus());
        goods.setPrice(new BigDecimal(String.valueOf(itemsSpec.getPriceDiscount() / 2)));
        goods.setUrl(itemsImg.getUrl());
        return goods;
    }

    @Override
    public GoodsAttr getGoodsAttr(String goodsId, String specId) {
        Example example = new Example(ItemsSpec.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId", goodsId);
        criteria.andEqualTo("id", specId);
        ItemsSpec itemsSpec = itemsSpecMapper.selectOneByExample(example);
        GoodsAttr goodsAttr = new GoodsAttr();
        goodsAttr.setName(itemsSpec.getName());
        goodsAttr.setStock(itemsSpec.getStock());
        goodsAttr.setDiscounts(itemsSpec.getDiscounts());
        goodsAttr.setPriceDiscount(itemsSpec.getPriceDiscount());
        goodsAttr.setPriceNormal(itemsSpec.getPriceNormal());
        return goodsAttr;
    }
}
