package com.cqyt.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cqyt.utils.ResponseUtils;
import com.cqyt.utils.wxutil.HttpRequest;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Hash;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

@ResponseBody
@Controller
@ApiOperation("登录")
public class LoginController {

    @PostMapping("/loginByCode")
    @ApiOperation("code登录")
    public ResponseUtils code2Id(String code) throws UnsupportedEncodingException {
        String parm = "";
        parm = "js_code="+code+"&"+"appid=wxe47e1e873ef317c7&secret=9126c58625ecfc0fe76a3e63ae365e45";
        String s = HttpRequest.sendPost("https://api.weixin.qq.com/sns/jscode2session?&grant_type=authorization_code", parm);
        JSONObject jo = JSON.parseObject(new String(s.getBytes(), "utf-8"));
        Object openid = jo.get("openid");
        if (openid == null){
            Object errmsg = jo.get("errmsg");
            return ResponseUtils.fail(errmsg.toString());
        }
        SecurityUtils.getSubject().getSession().setAttribute("userId",openid);
        /*
            这儿判断数据库中是否有本用户的数据
            没有的话要初始化用户数据
         */
        HashMap<String, String> stringStringHashMap = new HashMap<>();
        stringStringHashMap.put("token",SecurityUtils.getSubject().getSession().getId().toString());
        return ResponseUtils.ok("登录成功",stringStringHashMap);
    }
}
