package com.zengshen.goods.controller;

import com.zengshen.common.ApiRestResponse;
import com.zengshen.goods.service.CommentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.zengshen.common.constant.Constant.COMMON_PAGE_SIZE;

@RestController
@RequestMapping("mycomments")
public class CommentController {

    @Autowired
    private CommentService commentService;


    @PostMapping("/query")
    public ApiRestResponse query(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "page", value = "查询下一页的第几页", required = false)
            @RequestParam Integer page,
            @ApiParam(name = "pageSize", value = "分页的每一页显示的条数", required = false)
            @RequestParam Integer pageSize) {

        if (StringUtils.isBlank(userId)) {
            return ApiRestResponse.errorMsg("用户不能为空");
        }
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = COMMON_PAGE_SIZE;
        }
        return ApiRestResponse.success(commentService.getCommentByUserId(userId, page, pageSize));
    }
}
