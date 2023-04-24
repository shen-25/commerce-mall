package com.zengshen.common.enums;

public enum Sex {

    //
    woman(0, "女"),
    man(1, "男"),
    secret(2, "保密");

    private Integer type;
    private String value;

     Sex(Integer key, String value) {
        this.type = key;
        this.value = value;
    }

    public Integer getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

}
