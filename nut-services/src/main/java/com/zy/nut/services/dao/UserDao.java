package com.zy.nut.services.dao;

import com.zy.nut.common.beans.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Created by Administrator on 2016/10/30.
 */
@Mapper
public interface UserDao {

    long save(User user);

    int delAll();

    List<User> selectAll();
}
