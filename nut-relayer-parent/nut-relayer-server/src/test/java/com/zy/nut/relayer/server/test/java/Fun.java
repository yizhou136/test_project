package com.zy.nut.relayer.server.test.java;

/**
 * Created by Administrator on 2016/11/29.
 */
public interface Fun<T, R> {

    R  a(T t);

    default R apply(T t){
        System.out.println("xxx");
        return (R)t;
    }
}
