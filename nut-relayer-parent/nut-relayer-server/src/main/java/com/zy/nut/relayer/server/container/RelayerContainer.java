package com.zy.nut.relayer.server.container;


import com.zy.nut.relayer.common.container.AbstractContainer;
import com.zy.nut.relayer.common.container.Container;
import com.zy.nut.relayer.common.remoting.Server;

/**
 * Created by Administrator on 2016/11/7.
 */
public class RelayerContainer extends AbstractContainer implements Container{
    private Server server;


    public RelayerContainer(String propertiesFile){
        super(propertiesFile);
    }



    @Override
    public void start() {


    }
}
