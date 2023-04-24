package com.zengshen.order.controller;


import com.zengshen.common.ApiRestResponse;
import com.zengshen.common.constant.Constant;
import com.zengshen.common.utils.PageInfoResult;
import com.zengshen.order.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "用户中心我的订单", tags = {"用户中心我的订单相关接口"})
@RestController
@RequestMapping("myorders")
public class OrderMeController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/statusCounts")
    public ApiRestResponse statusCounts(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId) {

        if (StringUtils.isBlank(userId)) {
            return ApiRestResponse.errorMsg("用户不能为空");
        }

        return ApiRestResponse.success();
    }

    @ApiOperation(value = "查询订单列表", notes = "查询订单列表", httpMethod = "POST")
    @PostMapping("/query")
    public ApiRestResponse query(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "orderStatus", value = "订单状态", required = false)
            @RequestParam Integer orderStatus,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(userId)) {
            return ApiRestResponse.errorMsg("用户不存在");
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
        PageInfoResult myOrders = orderService.getMyOrders(userId, orderStatus, page, pageSize);
        return ApiRestResponse.success(myOrders);

    }
}
