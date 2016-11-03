package com.zy.nut.services.configuration.dubbo;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by zhougb on 2016/11/3.
 */
@Configuration
@ImportResource(locations ="classpath:dubbo/dubbo-nut-services.xml")
public class NutServicesConfiguration {

    static {
        System.setProperty("dubbo.properties.file", "classpath:dubbo/dubbo.properties");
    }
}
