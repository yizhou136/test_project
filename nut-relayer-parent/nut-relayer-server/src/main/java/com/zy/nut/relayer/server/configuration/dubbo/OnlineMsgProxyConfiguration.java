package com.zy.nut.relayer.server.configuration.dubbo;

import com.zy.nut.relayer.server.configuration.relayer.RelayerConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.annotation.Order;

/**
 * Created by zhougb on 2016/11/3.
 */
@Configuration
@Order(1)
@ImportResource(locations ="classpath:dubbo/dubbo-proxy.xml")
public class OnlineMsgProxyConfiguration {
    private static final Logger logger =
            LoggerFactory.getLogger(OnlineMsgProxyConfiguration.class);

    static {
        //System.setProperty("dubbo.properties.file", "classpath:dubbo/dubbo.properties");
    }

    @Bean
    public String testS(){
        logger.info("initializerRegisterList ");
        return "A";
    }
}
