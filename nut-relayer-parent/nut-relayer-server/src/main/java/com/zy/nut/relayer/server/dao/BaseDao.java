package com.zy.nut.relayer.server.dao;

import java.util.List;

/**
 * Created by zhougb on 2016/5/6.
 */
public interface BaseDao<T> {
    //for someone class
    T           get(long id);
    List<T>     getAll();
    long        save(T t);
    void        update(T t);
    void        batchUpdate(List<T> list);
    void        delete(long id);
    void        deleteAll();



    //public <T> List<T> list(String sql, Object[] params, Class<T> clazz)
    //void    deleteAll(Class<T> clazz);
    //int     queryCount(Object entity);


}