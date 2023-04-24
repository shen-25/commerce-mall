package com.zengshen.order.feginclient;

import com.zengshen.common.ApiRestResponse;
import com.zengshen.sdk.goods.Items;
import com.zengshen.sdk.goods.ItemsSpec;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "mall-goods", path = "/mall-goods")
public interface GoodsFeignClient {

    @GetMapping("/itemSpec/get")
    ApiRestResponse<ItemsSpec> getBySpecId(@RequestParam String specId);

    @GetMapping("/items/get/{itemId}")
    public ApiRestResponse<Items> getByItemId(@PathVariable String itemId);

    @GetMapping("/items/itemMainImg")
    public ApiRestResponse<String> itemMainImgById(@RequestParam String itemId);


    @PostMapping("/items/updateStock/{itemSpecId}")
    public ApiRestResponse<String> updateStock(@PathVariable String itemSpecId,
                                               @RequestParam Integer buyCounts);


}
