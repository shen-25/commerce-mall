package com.zengshen.cart.service;

import com.zengshen.cart.model.vo.CartItemVO;
import com.zengshen.cart.model.vo.CartVO;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @author word
 */
public interface CartService {

    /**
     * 将商品添加至购物车
     */
    CartItemVO addToCart(String itemId, String specId, String imgId, int num);

    /**
     * 获取购物车某个购物项
     */
    CartItemVO getCartItem(String specId);

    /**
     * 获取购物车里面的信息
     */
    CartVO getCart() throws ExecutionException, InterruptedException;

    /**
     * 清空购物车的数据
     */
    public void clearCartInfo(String cartKey);

    /**
     * 勾选购物项
     */
    void checkItem(String itemId, int check);

    /**
     * 改变商品数量
     */
    void changeItemCount(String specId, int num);


    /**
     * 删除购物项
     */
    void deleteIdCartInfo(String specId);

    /**
     * 获取当前用户的购物车所有商品项
     */
    List<CartItemVO> getUserCartItems();
}
