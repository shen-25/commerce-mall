package com.zengshen.sdk.goods;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;


@Data
@NoArgsConstructor
@AllArgsConstructor

public class ItemsSpec {

    private String id;

    private String itemId;


    private String name;


    private Integer stock;


    private BigDecimal discounts;


    private Integer priceDiscount;

    private Integer priceNormal;


    private Date createdTime;

    private Date updatedTime;
}