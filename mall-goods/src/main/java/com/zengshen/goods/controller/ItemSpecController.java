package com.zengshen.goods.controller;

import com.zengshen.common.ApiRestResponse;
import com.zengshen.goods.model.pojo.ItemsSpec;
import com.zengshen.goods.service.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/itemSpec")
public class ItemSpecController {

    @Autowired
    private ItemsService itemsService;

    @GetMapping("get")
    public ApiRestResponse<ItemsSpec> getBySpecId(@RequestParam String  specId) {
        return ApiRestResponse.success(itemsService.queryItemSpecById(specId));
    }

}
