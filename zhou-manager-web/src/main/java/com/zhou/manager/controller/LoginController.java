package com.zhou.manager.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @description: 登录controller
 * @author: zhoulei
 * @createTime: 2020-02-18 15:57
 **/
@RestController
@RequestMapping("/login")
public class LoginController {

    /**
      * @description 获取登录名称
      * @params []
      * @return java.util.Map<java.lang.String,java.lang.String>
      * @author zhoulei
      * @createtime 2020-02-18 16:01
      */
    @RequestMapping("/getName")
    public Map<String,String> getName(){
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Map<String,String> map = new HashMap<>();
        map.put("loginName",name);
        return map;
    }

}
