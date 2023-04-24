package com.zengshen.goods.controller;

import com.alibaba.fastjson.JSON;
import com.zengshen.common.ApiRestResponse;
import com.zengshen.common.enums.RedisKeyConstant;
import com.zengshen.goods.model.pojo.Carousel;
import com.zengshen.goods.model.pojo.Category;
import com.zengshen.goods.model.pojo.Items;
import com.zengshen.goods.model.vo.CategoryVO;
import com.zengshen.goods.model.vo.NewItemsVO;
import com.zengshen.goods.service.CarouselService;
import com.zengshen.goods.service.CategoryService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/carousel")
    public ApiRestResponse carousel() {
        List<Carousel> carouselList = carouselService.getCarouselList();
        return ApiRestResponse.success(carouselList);
    }

    /**
     * 首页分类展示需求：
     * 1. 第一次刷新主页查询大分类，渲染展示到首页
     * 2. 如果鼠标上移到大分类，则加载其子分类的内容，如果已经存在子分类，则不需要加载（懒加载）
     */

    /**
     * 获取商品分类(一级分类)
     */
    @GetMapping("/cats")
    public ApiRestResponse cats() {
        List<Category> rootCategory = categoryService.getRootCategoryList();
        return ApiRestResponse.success(rootCategory);
    }

    /**
     * 获取商品子分类
     */
    @GetMapping("/subCat/{rootCatId}")
    public ApiRestResponse subCat(@PathVariable String rootCatId) {
        if (StringUtils.isBlank(rootCatId)) {
            return ApiRestResponse.errorMsg("一级分类不存在");
        }
        List<CategoryVO> subCatList = categoryService.getSubCatList(rootCatId);
        return ApiRestResponse.success(subCatList);
    }

    /**
     * 查询每个一级分类下的最新6条商品数据
     */
    @GetMapping("/sixNewItems/{rootCatId}")
    public ApiRestResponse sixNewItems(@PathVariable String rootCatId) {

        if (StringUtils.isBlank(rootCatId)) {
            return ApiRestResponse.errorMsg("一级分类不存在");
        }
        List<NewItemsVO> sixNewItemsLazy = categoryService.getSixNewItemsLazy(rootCatId);
        return ApiRestResponse.success(sixNewItemsLazy);

    }


}
