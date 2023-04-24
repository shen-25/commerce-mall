package com.zengshen.goods.model.pojo;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "mall-goods", createIndex = true)
public class ItemsES {


    @Id
    @Field(type = FieldType.Text)
    private String itemId;

    @Field(index = true, analyzer = "ik_max_word")
    private String itemName;

    @Field(index = false)
    private String imgUrl;

    private Integer price;

    private Integer sellCounts;

}
