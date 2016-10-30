package com.zy.nut.services.serviceImpl;

import com.zy.nut.common.beans.User;
import com.zy.nut.common.service.UserService;
import com.zy.nut.services.Listener.MyTransEvent;
import com.zy.nut.services.dao.UserDao;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.springframework.test.context.transaction.AfterTransaction;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2016/10/30.
 */
@Service
public class UserServiceImpl implements UserService, ApplicationContextAware{
    private ApplicationContext applicationContext;

    @Autowired
    private UserDao userDao;

    @Transactional(isolation=Isolation.REPEATABLE_READ, readOnly = false, transactionManager = "")
    public User regUser(User user) {
        long uid = userDao.save(user);
        user.setUid(uid);

        applicationContext.publishEvent(new MyTransEvent("hahah"));
        return user;
    }


    @Transactional(isolation=Isolation.REPEATABLE_READ, readOnly = false)
    public int delAll() {
        return userDao.delAll();
    }


    @Transactional(isolation=Isolation.REPEATABLE_READ, readOnly = true)
    public List<User> selectAll() {
        List<User> res = userDao.selectAll();
        return res;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
