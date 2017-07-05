package com.zy.nut.relayer.server.configuration.dubbo;

import com.zy.nut.relayer.server.configuration.relayer.RelayerConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by zhougb on 2016/11/3.
 */
@Configuration
//@AutoConfigureBefore(RelayerConfiguration.class)
@ImportResource(locations ="classpath:dubbo/dubbo-proxy.xml")
public class OnlineMsgProxyConfiguration {

    static {
        System.setProperty("dubbo.properties.file", "classpath:dubbo/dubbo.properties");
    }
}
