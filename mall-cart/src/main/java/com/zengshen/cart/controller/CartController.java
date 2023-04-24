package com.zengshen.cart.controller;

import com.zengshen.cart.model.bo.ShopCartBO;
import com.zengshen.cart.model.vo.CartItemVO;
import com.zengshen.cart.service.CartService;
import com.zengshen.common.ApiRestResponse;
import com.zengshen.common.utils.JsonUtil;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("cart")
public class CartController {
    @Autowired
    private CartService cartService;

    /**
     * 添加商品到购物车
     */
    @PostMapping("/add/{goodsId}/{specId}/{num}")
    public ApiRestResponse<String> addCartItem(@PathVariable String goodsId,
                                               @PathVariable String specId,
                                               @RequestParam String imgId,
                                               @PathVariable Integer num) {
        cartService.addToCart(goodsId, specId, imgId, num);
        return ApiRestResponse.success();
    }

    /**
     * 订单服务调用：【购物车页面点击确认订单时】
     * 返回所有选中的商品项【从redis中取】
     * 并且要获取最新的商品价格信息，而不是redis中的数据
     * 获取当前用户的购物车所有商品项
     */
    @GetMapping( "/currentUserCartItems")
    public ApiRestResponse getCurrentCartItems() {
        List<CartItemVO> userCartItems = cartService.getUserCartItems();
        return ApiRestResponse.success(userCartItems);
    }

    /**
     * 更改选中状态
     */
    @PostMapping("/check/{specId}")
    public ApiRestResponse<String> checkItem(@PathVariable String specId,
                                             @RequestParam Integer checked) {
        cartService.checkItem(specId,checked);
        return ApiRestResponse.success();
    }

    /**
     * 改变商品数量
     */
    @PostMapping("/change/{specId}")
    public ApiRestResponse<String> countItem(@PathVariable String specId,
                                     @RequestParam Integer num) {
        cartService.changeItemCount(specId,num);
        return ApiRestResponse.success();
    }

    /**
     * 删除商品信息
     */
    @PostMapping(value = "/delete/{specId}")
    public ApiRestResponse<String> deleteItem(@PathVariable String specId) {
        cartService.deleteIdCartInfo(specId);
        return ApiRestResponse.success();
    }



}
