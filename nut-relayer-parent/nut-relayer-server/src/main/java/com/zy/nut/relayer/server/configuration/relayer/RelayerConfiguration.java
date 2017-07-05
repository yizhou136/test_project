package com.zy.nut.relayer.server.configuration.relayer;

import com.zy.nut.relayer.common.remoting.exchange.header.RelayerCodecSupport;
import com.zy.nut.relayer.common.transporter.ChannelInitializerRegister;
import com.zy.nut.relayer.server.container.RelayerHandlerInitializer;
import com.zy.nut.relayer.server.service.SpringNettyContainer;
import com.zy.nut.relayer.server.service.impl.SpringContainerImp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/4.
 */
@Configuration
//@AutoConfigureAfter(RelayerHandlerInitializer.class)
@EnableConfigurationProperties(RelayerProperties.class)

public class RelayerConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(RelayerConfiguration.class);
    @Autowired
    private RelayerProperties relayerProperties;

    @Bean(name = "springNettyContainer")
    public SpringNettyContainer springContainer(ApplicationContext applicationContext){
        SpringNettyContainer springNettyContainer = null;
        try {
            List<ChannelInitializerRegister> initializerRegisterList = new ArrayList<>();
            String[] beanNames = applicationContext.getBeanNamesForType(ChannelInitializerRegister.class);
            logger.info("initializerRegisterList beanNames:{}", beanNames);
            for (String beanName : beanNames) {
                initializerRegisterList.add((ChannelInitializerRegister)
                        applicationContext.getBean(beanName));
            }

            springNettyContainer = new SpringContainerImp(
                    relayerProperties.getConfigureUrl(),initializerRegisterList);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        System.setProperty("relayer.nodeName", springNettyContainer.getServer().getNodeName());
        return springNettyContainer;
    }

    @Bean(name = "relayerCodecSupport")
    public RelayerCodecSupport relayerCodecSupport(){
        return new RelayerCodecSupport();
    }
}
