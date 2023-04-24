package com.zengshen.sdk.goods;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 表名：items
 * 表注释：商品表 商品信息相关表：分类表，商品图片表，商品规格表，商品参数表
*/
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Items {

    private String id;


    private String itemName;


    private Integer catId;


    private Integer rootCatId;


    private Integer sellCounts;


    private Integer onOffStatus;


    private Date createdTime;


    private Date updatedTime;

    private String content;
}