package com.zy.nut.web.test.java;

/**
 * Created by zhougb on 2016/12/8.
 */
public interface InterfaceI {

    default void f(String name){
        System.out.println("print from InterfaceI:"+name);
    };
}
