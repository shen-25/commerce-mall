package com.zengshen.cart.feign;

import com.zengshen.common.ApiRestResponse;
import com.zengshen.sdk.goods.Goods;
import com.zengshen.sdk.goods.GoodsAttr;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

/**
 * @author word
 */
@FeignClient(name = "mall-goods", path = "/mall-goods/goods",
        fallback = GoodsFeignClientFallBack.class)
public interface GoodsFeignClient {

    @GetMapping("/{goodsId}/{specId}")
    public ApiRestResponse<Goods> getGoods(@PathVariable String goodsId,
                                           @PathVariable String specId,
                                           @RequestParam String imgId);

    @GetMapping("/attrs/{goodsId}/{specId}")
    public ApiRestResponse<GoodsAttr> getGoodsAttr(@PathVariable String goodsId,
                                                   @PathVariable String specId);

    @GetMapping("/price/{goodsId}/{specId}")
    public ApiRestResponse<BigDecimal> getPrice(@PathVariable String goodsId,
                                                @PathVariable String specId);
}
