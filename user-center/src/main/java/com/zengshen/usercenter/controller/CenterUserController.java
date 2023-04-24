package com.zengshen.usercenter.controller;

import com.zengshen.common.ApiRestResponse;
import com.zengshen.common.utils.JsonUtil;
import com.zengshen.mvc.utils.CookieUtil;
import com.zengshen.usercenter.model.bo.CenterUserBO;
import com.zengshen.usercenter.model.pojo.Users;
import com.zengshen.usercenter.service.CenterUserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("userInfo")
public class CenterUserController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private HttpServletResponse response;

    @Autowired
    private CenterUserService centerUserService;

//    @Autowired
//    private MinioTemplate minioTemplate;


    @PostMapping("update")
    public ApiRestResponse updateUser(@RequestParam String userId,
                                      @RequestBody @Valid CenterUserBO centerUserBO) {

        Users users = centerUserService.updateUserInfo(userId, centerUserBO);
        return ApiRestResponse.success(users);

    }
    @ApiOperation(value = "用户头像修改", notes = "用户头像修改", httpMethod = "POST")
    @PostMapping("uploadFace")
    public ApiRestResponse uploadFace(
            @ApiParam(name = "userId", value = "用户id", required = true)
            @RequestParam String userId,
            @ApiParam(name = "file", value = "用户头像", required = true)
                    MultipartFile file) throws Exception {
        String filename = UUID.randomUUID().toString();
        if (StringUtils.isNotBlank(file.getOriginalFilename())) {
            filename += file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        }

//        minioTemplate.uploadFile(filename, file.getInputStream());
//        String finalUserFaceUrl =  minioTemplate.getFileHost() + "/" + minioTemplate.getBucketName() + "/" + filename;
        String finalUserFaceUrl = "";

        // 更新用户头像到数据库
        Users userResult = centerUserService.updateUserFace(userId, finalUserFaceUrl);
        userResult.setPassword(null);
        userResult.setMobile(null);
        userResult.setEmail(null);
        userResult.setRealname(null);

        CookieUtil.setCookie(request, response, "user",
                JsonUtil.objectToString(userResult), true);
        // TODO 后续要改，增加令牌token，会整合进redis，分布式会话

        return ApiRestResponse.success();
    }
}
