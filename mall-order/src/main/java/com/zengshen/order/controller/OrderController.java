package com.zengshen.order.controller;

import com.zengshen.common.ApiRestResponse;
import com.zengshen.common.constant.CookieConstant;
import com.zengshen.common.enums.PayMethod;
import com.zengshen.mvc.utils.CookieUtil;
import com.zengshen.order.model.bo.SubmitOrderBO;
import com.zengshen.order.model.vo.OrderVO;
import com.zengshen.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private OrderService orderService;

    @PostMapping("/create")
    public ApiRestResponse create(@RequestBody SubmitOrderBO submitOrderBO) {

        if (!submitOrderBO.getPayMethod().equals(PayMethod.WEIXIN.type)
                && !submitOrderBO.getPayMethod().equals(PayMethod.ALIPAY.type)) {
            return ApiRestResponse.errorMsg("支付方式不支持！");
        }

        // 1. 创建订单
        OrderVO orderVO = orderService.addOrder(submitOrderBO);

        // 2. 创建订单以后，移除购物车中已结算（已提交）的商品
        CookieUtil.setCookie(request, response, CookieConstant.SHOP_CART, "");
        return ApiRestResponse.success(orderVO.getOrderId());
    }

    @PostMapping("/getPrice")
    public ApiRestResponse getPrice(@RequestParam String orderId) {

        BigDecimal price = orderService.getPrice(orderId);
        return ApiRestResponse.success(price);
    }
}
