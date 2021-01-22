package com.cqyt.controller;

import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class testController {
    @GetMapping("/test")
    @ApiOperation("路由测试，成功返回成功")

    public String test(){
        String userId = (String) SecurityUtils.getSubject().getSession().getAttribute("userId");
        return userId;
    }
}
