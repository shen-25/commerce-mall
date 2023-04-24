package com.zengshen.cart.service.impl;

import com.alibaba.fastjson.JSON;
import com.zengshen.cart.feign.GoodsFeignClient;
import com.zengshen.cart.interceptor.CartInterceptor;
import com.zengshen.sdk.goods.Goods;
import com.zengshen.cart.model.vo.CartItemVO;
import com.zengshen.cart.model.vo.CartVO;
import com.zengshen.cart.service.CartService;
import com.zengshen.common.ApiRestResponse;
import com.zengshen.common.enums.RedisKeyConstant;
import com.zengshen.sdk.goods.GoodsAttr;
import com.zengshen.sdk.user.UsersInfoToCart;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * @author word
 */
@Slf4j
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ThreadPoolExecutor poolExecutor;

    @Autowired
    private GoodsFeignClient goodsFeignClient;

    /**
     *  封装购物车类 所有商品，所有商品的价格
     *  整合登录状态与未登录状态
     */
    @Override
    public CartVO getCart() throws ExecutionException, InterruptedException {
        CartVO cartVO = new CartVO();
        UsersInfoToCart usersInfoToCart = CartInterceptor.toThreadLocal.get();
        CartInterceptor.toThreadLocal.remove();
        log.info("用户信息为: {}", JSON.toJSONString(usersInfoToCart));
        String userId = usersInfoToCart.getUserId();
        if (StringUtils.isNotBlank(userId)) {
            // 1. 临时购物车的 key
            String temptCartKey = RedisKeyConstant.cart_user.getKey() + usersInfoToCart.getUserKey();
            // 1.1 获取临时购物车的数据
            List<CartItemVO> cartItems = this.getCartItems(temptCartKey);
            // 1.2 临时购物车的数据还没有合并
            if (!CollectionUtils.isEmpty(cartItems)) {
                // 2.3 进行合并操作
                for (CartItemVO cartItemVO : cartItems) {
                    addToCart(cartItemVO.getGoodsId(), cartItemVO.getSpecId(), cartItemVO.getSpecId(),cartItemVO.getCount());
                }
                // 2.4 清除临时购物车的数据
                clearCartInfo(temptCartKey);
            }
            // 2. 登录后的购物车 key
            String cartKey = RedisKeyConstant.cart_user.getKey() + userId;
            // 3. 获取登录后的购物车数据(包括了合并的临时数据和已经登录的购物车数据)
            List<CartItemVO> cartItemVOList = this.getCartItems(cartKey);
            cartVO.setItems(cartItemVOList);
        }else{
            // 没有登录
            String cartKey = RedisKeyConstant.cart_user.getKey() + usersInfoToCart.getUserKey();
            // 获取临时购物车中的所有购物项
            List<CartItemVO> cartItems = getCartItems(cartKey);
            cartVO.setItems(cartItems);
        }
        return cartVO;
    }

    /**
     * 添加商品到购物车
     */
    @Override
    public CartItemVO addToCart(String itemId, String specId, String imgId, int num) {
        // 获取当前用户的购物车数据的 hash
        BoundHashOperations<String, Object, Object> cartOps = this.getCartOps();
        // 判断 Redis 中是否有该商品信息
        String productStr = (String)cartOps.get(specId);

        // 开启第一个任务
        if (StringUtils.isBlank(productStr)) {
            CartItemVO cartItemVO = new CartItemVO();
            CompletableFuture<Void> getGoodsInfoFuture = CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    ApiRestResponse<Goods> item = goodsFeignClient.getGoods(itemId, specId, imgId);
                    Goods goods = item.getData();
                    log.info("消息体： getmsg: {}", item.getMsg());
                    log.info("消息体： getStatus: {}", item.getStatus());
                    cartItemVO.setGoodsId(itemId);
                    cartItemVO.setSpecId(specId);

                    cartItemVO.setImage(goods.getUrl());
                    cartItemVO.setPrice(goods.getPrice());
                    cartItemVO.setTitle(goods.getItemName());
                    cartItemVO.setCheck(true);
                    cartItemVO.setCount(num);
                }
            }, poolExecutor);

            // 开启第二个异步任务
            CompletableFuture<Void> getGoodsAttrFuture = CompletableFuture.runAsync(new Runnable() {
                @Override
                public void run() {
                    ApiRestResponse<GoodsAttr> goodsAttr = goodsFeignClient.getGoodsAttr(itemId,specId);
                     GoodsAttr data = goodsAttr.getData();
                     cartItemVO.setGoodsAttr(data);
                }
            }, poolExecutor);

            try {
                CompletableFuture.allOf(getGoodsAttrFuture, getGoodsInfoFuture).get();
            } catch (InterruptedException | ExecutionException e) {
                log.info("捕获异常 ： {}", e.getMessage(), e);
            }
            cartOps.put(specId, JSON.toJSONString(cartItemVO));
            return cartItemVO;
        }else{
            // 购物车有此商品，修改数量
            CartItemVO cartItemVO = JSON.parseObject(productStr, CartItemVO.class);
            cartItemVO.setCount(cartItemVO.getCount() + num);
            cartOps.put(specId, JSON.toJSONString(cartItemVO));
            return cartItemVO;
        }

    }

    @Override
    public CartItemVO getCartItem(String specId) {
        //拿到要操作的购物车信息
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        String redisValue = (String) cartOps.get(specId);
        return JSON.parseObject(redisValue, CartItemVO.class);
    }


    @Override
    public void clearCartInfo(String cartKey) {
        redisTemplate.delete(cartKey);
    }


    @Override
    public void checkItem(String specId, int check) {
        // 查询购物车中的商品
        CartItemVO cartItem = getCartItem(specId);
        if (cartItem == null) {
            return;
        }
        cartItem.setCheck(check == 1);
        BoundHashOperations<String, Object, Object> cartOps = getCartOps();
        cartOps.put(specId, JSON.toJSONString(cartItem));
    }

    /**
     * 修改购物项的数量
     */
    @Override
    public void changeItemCount(String specId, int num) {
        CartItemVO cartItem = this.getCartItem(specId);
        if (cartItem == null) {
            return;
        }
        cartItem.setCount(num);
        BoundHashOperations<String, Object, Object> cartOps = this.getCartOps();
        cartOps.put(specId, JSON.toJSONString(cartItem));
    }


    /**
     * 删除购物项
     */
    @Override
    public void deleteIdCartInfo(String specId) {
        BoundHashOperations<String, Object, Object> cartOps = this.getCartOps();
        cartOps.delete(specId);
    }

    /**
     * 远程调用：订单服务调用 更新最新价格
     */
    @Override
    public List<CartItemVO> getUserCartItems() {

        // 获取用户登录信息
        UsersInfoToCart usersInfoToCart = CartInterceptor.toThreadLocal.get();
        CartInterceptor.toThreadLocal.remove();
        String userId = usersInfoToCart.getUserId();
        // 用户没有登录
        if (StringUtils.isBlank(userId)) {
            return null;
        }
        String cartKey = RedisKeyConstant.cart_user.getKey() + userId;
        // 获取购物车项
        List<CartItemVO> cartItems = getCartItems(cartKey);
        if (cartItems == null) {
            return null;
        }
        // 过滤掉没有勾选的商品
        return cartItems.stream()
                .filter(CartItemVO::getCheck).map(cartItemVO -> {
                    System.out.println(cartItemVO);
                // 更新为最新的价格
                ApiRestResponse<BigDecimal> price = goodsFeignClient.getPrice(cartItemVO.getGoodsId(), cartItemVO.getSpecId());
                BigDecimal data = price.getData();
                // 更新为最新的价格
                cartItemVO.setPrice(data);
                return cartItemVO;
                }).collect(Collectors.toList());
    }

    /**
     * 获取购物车里面的数据
     * key = [cart_user:${userId}]
     * 没有数据返回 null
     */
    private List<CartItemVO> getCartItems(String cartKey) {
        // 1. 定义一个BoundHashOperations
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(cartKey);
        // 2.获取map中的值
        List<Object> values = operations.values();
        if (!CollectionUtils.isEmpty(values)) {
            return values.stream()
                    .map(obj -> JSON.parseObject((String)obj, CartItemVO.class))
                    .collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 获取到我们要操作的购物车
     * 简化代码：
     *      1、判断是否登录，拼接key
     *      2、数据是hash类型，所以每次要调用两次key【直接绑定外层key】
     *          第一层 key：cart_user:${userId}
     *          第二层 key：uuid
     */
    private BoundHashOperations<String, Object, Object> getCartOps() {
        UsersInfoToCart usersInfoToCart = CartInterceptor.toThreadLocal.get();
        CartInterceptor.toThreadLocal.remove();
        String userId = usersInfoToCart.getUserId();
        String cartKey = "";
        if (StringUtils.isNotBlank(userId)) {
            // cart_user:${userId}
            cartKey = RedisKeyConstant.cart_user.getKey() + userId;
        }else {
            //
            cartKey = RedisKeyConstant.cart_user.getKey() + usersInfoToCart.getUserKey();
        }

        // 绑定指定的key操作Redis
        BoundHashOperations<String, Object, Object> operations = redisTemplate.boundHashOps(cartKey);
        return operations;

    }

}
