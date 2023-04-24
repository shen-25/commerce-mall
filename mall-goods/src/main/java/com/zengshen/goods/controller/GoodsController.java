package com.zengshen.goods.controller;

import com.zengshen.common.ApiRestResponse;
import com.zengshen.goods.service.ItemsService;
import com.zengshen.sdk.goods.Goods;
import com.zengshen.sdk.goods.GoodsAttr;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/goods")
public class GoodsController {


    @Autowired
    private ItemsService itemsService;

    @GetMapping("/{goodsId}/{specId}")
    public ApiRestResponse<Goods> getGoods(@PathVariable String goodsId,
                                           @PathVariable String specId,
                                           @RequestParam String imgId) {
        Goods goods = itemsService.getGoods(goodsId, specId, imgId);
        return ApiRestResponse.success(goods);
    }

    @GetMapping("/attrs/{goodsId}/{specId}")
    public ApiRestResponse<GoodsAttr> getGoodsAttr(@PathVariable String goodsId,
                                                   @PathVariable String specId) {
        return ApiRestResponse.success(itemsService.getGoodsAttr(goodsId, specId));
    }

    @GetMapping("/price/{goodsId}/{specId}")
    public ApiRestResponse<BigDecimal> getPrice(@PathVariable String goodsId,
                                                @PathVariable String specId) {
        return ApiRestResponse.success(itemsService.getPrice(goodsId, specId));
    }


}
