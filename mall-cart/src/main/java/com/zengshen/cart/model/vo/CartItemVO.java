package com.zengshen.cart.model.vo;

import com.zengshen.sdk.goods.GoodsAttr;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author word
 */
public class CartItemVO {

    private String  goodsId;                // skuId
    private Boolean check = true;         // 是否选中
    private String title;                 // 标题
    private String image;                 // 图片
    private GoodsAttr goodsAttr;   // 商品销售属性
    private BigDecimal price;             // 单价
    private Integer count;                // 当前商品数量
    private BigDecimal totalPrice;        // 总价

    private String specId ;  // 规格Id

    private String imgId; // 图片id


    /**
     * 计算当前购物项总价
     */
    public BigDecimal getTotalPrice() {
        return this.price.multiply(new BigDecimal("" + this.count));
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }


    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public GoodsAttr getGoodsAttr() {
        return goodsAttr;
    }

    public void setGoodsAttr(GoodsAttr goodsAttr) {
        this.goodsAttr = goodsAttr;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getSpecId() {
        return specId;
    }

    public void setSpecId(String specId) {
        this.specId = specId;
    }

    public String getImgId() {
        return imgId;
    }

    public void setImgId(String imgId) {
        this.imgId = imgId;
    }
}
