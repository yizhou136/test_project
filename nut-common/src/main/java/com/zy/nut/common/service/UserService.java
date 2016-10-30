package com.zy.nut.common.service;

import com.zy.nut.common.beans.User;

import java.util.List;

/**
 * Created by Administrator on 2016/10/30.
 */
public interface UserService{
    public User regUser(User user);

    public int delAll();

    public List<User> selectAll();
}
