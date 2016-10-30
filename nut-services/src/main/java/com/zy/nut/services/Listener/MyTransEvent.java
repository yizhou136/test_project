package com.zy.nut.services.Listener;

import org.springframework.context.ApplicationEvent;

/**
 * Created by Administrator on 2016/10/30.
 */
public class MyTransEvent extends ApplicationEvent {


    public MyTransEvent(Object source) {
        super(source);
    }
}
