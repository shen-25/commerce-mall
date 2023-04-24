package com.zengshen.common.enums;

public enum CommentLevel {

    /**
     * 评分等级
     */
    GOOD(1, "好评"),
    NORMAL(2, "中评"),
    BAD(3, "差评");

    private Integer score;
    private String desc;

     CommentLevel(Integer score, String desc) {
        this.score = score;
        this.desc = desc;
    }

    public Integer getScore() {
        return score;
    }

    public String getDesc() {
        return desc;
    }
}
