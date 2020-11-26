package com.xxy.controller;

import com.xxy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 根据id 获取用户
     * @param id
     * @return 用户的string
     */
    @RequestMapping("{id}")
    public String queryById(@PathVariable Long id) {
        return userService.queryById(id).toString();
    }
}
