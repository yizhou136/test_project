package com.zy.nut.relayer.common.container;

/**
 * Created by Administrator on 2016/11/7.
 */
public interface Container {

    void configure(String url);

    void reconfigure(String url);

    void start();

    void stop();
}
