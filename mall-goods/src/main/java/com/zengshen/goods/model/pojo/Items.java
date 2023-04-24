package com.zengshen.goods.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 表名：items
 * 表注释：商品表 商品信息相关表：分类表，商品图片表，商品规格表，商品参数表
*/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "items")
public class Items {
    /**
     * 商品主键id
     */
    @Id
    @GeneratedValue(generator = "JDBC")
    private String id;

    /**
     * 商品名称 商品名称
     */
    @Column(name = "item_name")
    private String itemName;

    /**
     * 分类外键id 分类id
     */
    @Column(name = "cat_id")
    private Integer catId;

    /**
     * 一级分类外键id
     */
    @Column(name = "root_cat_id")
    private Integer rootCatId;

    /**
     * 累计销售 累计销售
     */
    @Column(name = "sell_counts")
    private Integer sellCounts;

    /**
     * 上下架状态 上下架状态,1:上架 2:下架
     */
    @Column(name = "on_off_status")
    private Integer onOffStatus;

    /**
     * 创建时间
     */
    @Column(name = "created_time")
    private Date createdTime;

    /**
     * 更新时间
     */
    @Column(name = "updated_time")
    private Date updatedTime;

    /**
     * 商品内容 商品内容
     */
    private String content;
}