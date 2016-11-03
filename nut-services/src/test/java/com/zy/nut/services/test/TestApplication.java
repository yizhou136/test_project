package com.zy.nut.services.test;

import com.zy.nut.common.beans.User;
import com.zy.nut.common.service.UserService;
import com.zy.nut.services.BootNutServices;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.AssertionErrors;

import java.io.IOException;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/10/30.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BootNutServices.class)
public class TestApplication{
    private Logger logger = LoggerFactory.getLogger(TestApplication.class);

    @Autowired
    private UserService userService;

    @Before
    public void testDelAllUser(){
        int size = userService.delAll();
        logger.info("testDelAllUser size:{}", size);


        List<User> list = userService.selectAll();

        logger.debug("testLoadAll1 :{}", list);
    }

    @Test
    public void testUserService(){
        Random random = new Random();
        String uname = "zy"+random.nextInt();
        User user = new User();
        user.setAge((byte)12);
        user.setUname(uname);
        user.setIp(1315612342);
        user.setLoginTime(123712341);
        User regedUser = userService.regUser(user);
        logger.debug("regedUser :{}", regedUser);
        Assert.assertTrue("hahaha", regedUser.getUid()>0);
    }

    @After
    public void testLoadAll(){
        List<User> list = userService.selectAll();

        logger.debug("testLoadAll2 :{}", list);
    }
}