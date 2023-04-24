package com.zengshen.goods.controller;

import com.zengshen.common.ApiRestResponse;
import com.zengshen.common.constant.Constant;
import com.zengshen.common.utils.PageInfoResult;
import com.zengshen.goods.model.pojo.Items;
import com.zengshen.goods.model.pojo.ItemsImg;
import com.zengshen.goods.model.pojo.ItemsParam;
import com.zengshen.goods.model.pojo.ItemsSpec;
import com.zengshen.goods.model.vo.CommentLevelCountsVO;
import com.zengshen.goods.model.vo.ItemInfoVO;
import com.zengshen.goods.model.vo.ShopCartVO;
import com.zengshen.goods.service.CommentService;
import com.zengshen.goods.service.ItemsService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("items")
public class ItemsController {

    @Autowired
    private ItemsService itemService;

    @Autowired
    private CommentService commentService;


    @GetMapping("/info/{itemId}")
    public ApiRestResponse info(@PathVariable String itemId) {

        // 整合返回item的数据

        Items item = itemService.selectItemById(itemId);
        List<ItemsImg> itemImgList = itemService.selectItemImgList(itemId);
        List<ItemsSpec> itemsSpecList = itemService.selectItemSpecList(itemId);
        ItemsParam itemsParam = itemService.selectItemParam(itemId);

        ItemInfoVO itemInfoVO = new ItemInfoVO();
        itemInfoVO.setItem(item);
        itemInfoVO.setItemImgList(itemImgList);
        itemInfoVO.setItemSpecList(itemsSpecList);
        itemInfoVO.setItemParams(itemsParam);

        return ApiRestResponse.success(itemInfoVO);
    }

    @GetMapping("/commentLevel")
    public ApiRestResponse commentLevel(@RequestParam String itemId) {
        if (StringUtils.isBlank(itemId)) {
            return ApiRestResponse.errorMsg("itemId不能为空");
        }
        CommentLevelCountsVO commentLevelCounts = commentService.getCommentLevelCounts(itemId);
        return ApiRestResponse.success(commentLevelCounts);
    }

    @GetMapping("comments")
    public ApiRestResponse comments(@RequestParam String itemId, @RequestParam(required = false) String level,
                                    @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {
        if (StringUtils.isBlank(itemId)) {
            return ApiRestResponse.errorMsg("itemId不能为空");
        }
        if (page == null) {
            page = Constant.COMMON_PAGE;
        }
        if (pageSize == null) {
            pageSize = Constant.COMMON_PAGE_SIZE;
        }
        if (page > Constant.MAX_PAGE) {
            page = Constant.MAX_PAGE;
        }
        if (pageSize > Constant.MAX_PAGE_SIZE) {
            page = Constant.MAX_PAGE_SIZE;
        }

        PageInfoResult pageInfoResult = commentService.getCommentList(itemId, level, page, pageSize);
        return ApiRestResponse.success(pageInfoResult);

    }

    /**
     * 搜索商品列表
     * @param catId 三级分类id
     * @param sort 排序规则
     */
    @GetMapping("catItems")
    public ApiRestResponse catItems(@RequestParam String catId, @RequestParam(required = false) String sort,
                                    @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {
        if (StringUtils.isBlank(catId)) {
            return ApiRestResponse.errorMsg("catId 不能为空");
        }
        if (page == null) {
            page = Constant.COMMON_PAGE;
        }
        if (pageSize == null) {
            pageSize = Constant.ITEMS_COMMON_PAGE_SIZE;
        }
        if (page > Constant.MAX_PAGE) {
            page = Constant.MAX_PAGE;
        }
        if (pageSize > Constant.MAX_PAGE_SIZE) {
            page = Constant.MAX_PAGE_SIZE;
        }
        PageInfoResult pageInfoResult = itemService.searchItemsByCategoryId(catId, sort, page, pageSize);
        return ApiRestResponse.success(pageInfoResult);

    }

    /**
     * 搜索商品列表
     * @param keywords 三级分类id
     * @param sort 排序规则
     */
    @GetMapping("search")
    public ApiRestResponse searchByKeyword(@RequestParam String keywords, @RequestParam(required = false) String sort,
                                    @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {
        if (StringUtils.isBlank(keywords)) {
            return ApiRestResponse.errorMsg("keywords 不能为空");
        }
        if (page == null) {
            page = Constant.COMMON_PAGE;
        }
        if (pageSize == null) {
            pageSize = Constant.ITEMS_COMMON_PAGE_SIZE;
        }
        if (page > Constant.MAX_PAGE) {
            page = Constant.MAX_PAGE;
        }
        if (pageSize > Constant.MAX_PAGE_SIZE) {
            page = Constant.MAX_PAGE_SIZE;
        }
        PageInfoResult pageInfoResult = itemService.searchByKeyword(keywords, sort, page, pageSize);
        return ApiRestResponse.success(pageInfoResult);

    }


    // 用于用户长时间未登录网站，刷新购物车中的数据（主要是商品价格），类似京东淘宝
    @ApiOperation(value = "根据商品规格ids查找最新的商品数据", notes = "根据商品规格ids查找最新的商品数据", httpMethod = "GET")
    @GetMapping("/refresh")
    public ApiRestResponse refresh(
            @ApiParam(name = "itemSpecIds", value = "拼接的规格ids", required = true, example = "1001,1003,1005")
            @RequestParam String itemSpecIds) {

        if (StringUtils.isBlank(itemSpecIds)) {
            return ApiRestResponse.success();
        }

        List<ShopCartVO> list = itemService.selectItemsBySpecIds(itemSpecIds);
        return ApiRestResponse.success(list);
    }

    @GetMapping("get/{itemId}")
    public ApiRestResponse<Items> getByItemId(@PathVariable String itemId) {
        Items items = itemService.selectItemById(itemId);
        return ApiRestResponse.success(items);
    }

    @GetMapping("itemMainImg")
    public ApiRestResponse<String > itemMainImgById(@RequestParam String itemId) {
        String s = itemService.queryItemMainImgById(itemId);
        return ApiRestResponse.success(s);
    }


    /**
     * 扣减库存
     */
    @PostMapping("/updateStock/{itemSpecId}")
    public ApiRestResponse<String > updateStock(@PathVariable String itemSpecId,
                                                 @RequestParam Integer buyCounts) {
        itemService.decreaseItemSpecStock(itemSpecId, buyCounts);
        return ApiRestResponse.success();
    }

}
