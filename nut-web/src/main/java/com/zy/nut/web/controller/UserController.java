package com.zy.nut.web.controller;

import com.zy.nut.common.beans.User;
import com.zy.nut.common.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2016/10/30.
 */
@Controller("user")
public class UserController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserService userService;

    @RequestMapping("save")
    public User saveUser(User userArg){
        User user = userService.regUser(userArg);
        logger.debug("reguser ret:%p", user);
        return user;
    }
}