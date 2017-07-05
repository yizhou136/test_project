package com.zy.nut.relayer.server.service.impl;

import com.zy.nut.relayer.common.amqp.ServerendAMQPClient;
import com.zy.nut.relayer.common.configure.Configuration;
import com.zy.nut.relayer.common.container.AbstractContainer;
import com.zy.nut.relayer.common.container.ConnectToLeadingClientContainer;
import com.zy.nut.relayer.common.remoting.RemotingException;
import com.zy.nut.relayer.common.remoting.Server;
import com.zy.nut.relayer.common.remoting.exchange.*;
import com.zy.nut.relayer.common.transporter.ChannelInitializerRegister;
import com.zy.nut.relayer.common.transporter.netty.NettyServer;
import com.zy.nut.relayer.server.service.SpringNettyContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/4.
 */
public class SpringContainerImp extends AbstractContainer implements SpringNettyContainer{
    private static final Logger logger = LoggerFactory.getLogger(SpringContainerImp.class);

    private NettyServer server;
    private ServerendAMQPClient serverendAMQPClient;
    private ConnectToLeadingClientContainer connectToLeadingClientContainer;

    private List<ChannelInitializerRegister> initializerRegisterList;

    public SpringContainerImp(URL url, List<ChannelInitializerRegister> initializerRegisterList)throws Throwable{
        super(url);
        this.initializerRegisterList = initializerRegisterList;
        init();
    }

    @Override
    public void init() throws Throwable {
        Configuration configuration = getConfiguration();
        if (configuration == null)
            return;

        try {
            server = new NettyServer(configuration, null, initializerRegisterList);
        } catch (RemotingException e) {
            e.printStackTrace();
            throw e;
        }

        /*if (configuration.isClusterLeader()){//for leading server
            serverendAMQPClient = new ServerendAMQPClient(configuration, this);
        }else {
            connectToLeadingClientContainer = new ConnectToLeadingClientContainer(server,configuration);
            serverendAMQPClient = new ServerendAMQPClient(configuration, null);
        }*/
    }


    @Override
    public Server getServer() {
        return server;
    }
}
