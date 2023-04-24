package com.zengshen.sdk.goods;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Goods {

    private String id;

    private String itemName;

    private Integer onOffStatus;

    private String url;

    private BigDecimal price;

}
