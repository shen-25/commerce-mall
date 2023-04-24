package com.zengshen.cart.feign;

import com.zengshen.common.ApiRestResponse;
import com.zengshen.common.enums.BusinessExceptionEnum;
import com.zengshen.sdk.goods.Goods;
import com.zengshen.sdk.goods.GoodsAttr;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Sentinel 对 OpenFeign 接口的降级策略
 * @author word
 */
@Slf4j
@Component
public class GoodsFeignClientFallBack implements GoodsFeignClient{

    @Override
    public ApiRestResponse<Goods> getGoods(String goodsId, String specId, String imgId) {
        return ApiRestResponse.errorEnum(BusinessExceptionEnum.FALL_BACK);
    }

    @Override
    public ApiRestResponse<GoodsAttr> getGoodsAttr(String goodsId, String specId) {
        return ApiRestResponse.errorEnum(BusinessExceptionEnum.FALL_BACK);
    }

    @Override
    public ApiRestResponse<BigDecimal> getPrice(String goodsId, String specId) {
        return ApiRestResponse.errorEnum(BusinessExceptionEnum.FALL_BACK);
    }
}
