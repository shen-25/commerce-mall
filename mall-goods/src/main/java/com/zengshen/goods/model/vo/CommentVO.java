package com.zengshen.goods.model.vo;

import lombok.Data;

import java.util.Date;

@Data
public class CommentVO {
    private Integer commentLevel;
    private String content;
    private String specName;
    private Date createdTime;
    private String userFace;
    private String nickname;
}
