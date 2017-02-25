package com.zy.nut.relayer.server.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by zhougb on 2016/11/3.
 */
@Configuration
@ImportResource(locations ="classpath:dubbo/dubbo-nut.xml")
public class DubboConfiguration {

    static {
        System.setProperty("dubbo.properties.file", "classpath:dubbo/dubbo.properties");
    }
}
