package com.zengshen.common.enums;

public enum YesOrNo {
    // 是还是不是
    NO(0, "否"),
    YES(1, "是");

    public int type;
    public String desc;

     YesOrNo(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return type;
    }

    public String getDesc() {
        return desc;
    }

    public void setType(int type) {
        this.type = type;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
