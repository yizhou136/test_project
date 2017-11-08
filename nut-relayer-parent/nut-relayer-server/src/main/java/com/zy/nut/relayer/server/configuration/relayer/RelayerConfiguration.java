package com.zy.nut.relayer.server.configuration.relayer;

import com.zy.nut.relayer.common.remoting.exchange.header.RelayerCodecSupport;
import com.zy.nut.relayer.server.service.SpringNettyContainer;
import com.zy.nut.relayer.server.service.impl.SpringContainerImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

/**
 * Created by Administrator on 2016/2/4.
 */
@Configuration
@Order(0)
@EnableConfigurationProperties(RelayerProperties.class)

public class RelayerConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(RelayerConfiguration.class);
    @Autowired
    private RelayerProperties relayerProperties;

    @Bean(name = "springNettyContainer")
    public SpringNettyContainer springContainer(ApplicationContext applicationContext){
        SpringNettyContainer springNettyContainer = null;
        try {
            springNettyContainer = new SpringContainerImp(
                    relayerProperties.getConfigureUrl());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return springNettyContainer;
    }

    @Bean(name = "relayerCodecSupport")
    public RelayerCodecSupport relayerCodecSupport(){
        return new RelayerCodecSupport();
    }
}
