package com.zengshen.sdk.user;


import lombok.Data;

import java.util.Date;

@Data
public class UserAddress {


    private String id;


    private String userId;

    private String receiver;

    private String mobile;

    private String province;


    private String city;

    private String district;

    private String detail;

    private String extand;

    private Integer isDefault;

    private Date createdTime;


    private Date updatedTime;
}


