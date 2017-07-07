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
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/4.
 */
public class SpringContainerImp extends AbstractContainer implements SpringNettyContainer, InitializingBean, ApplicationContextAware{
    private static final Logger logger = LoggerFactory.getLogger(SpringContainerImp.class);

    private NettyServer server;
    private ServerendAMQPClient serverendAMQPClient;
    private ConnectToLeadingClientContainer connectToLeadingClientContainer;

    private List<ChannelInitializerRegister> initializerRegisterList;

    private ApplicationContext applicationContext;

    public SpringContainerImp(URL url)throws Throwable{
        super(url);
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

    @Override
    public void afterPropertiesSet() throws Exception {
        logger.info("afterPropertiesSet");
        List<ChannelInitializerRegister> initializerRegisterList = new ArrayList<>();
        String[] beanNames = applicationContext.getBeanNamesForType(ChannelInitializerRegister.class);
        logger.info("initializerRegisterList beanNames:{}", beanNames);
        for (String beanName : beanNames) {
            initializerRegisterList.add((ChannelInitializerRegister)
                    applicationContext.getBean(beanName));
        }

        this.initializerRegisterList = initializerRegisterList;
        try {
            init();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
