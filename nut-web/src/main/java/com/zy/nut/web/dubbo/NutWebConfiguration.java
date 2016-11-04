package com.zy.nut.web.dubbo;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by zhougb on 2016/11/4.
 */
@Configuration
@ImportResource(locations ="classpath:dubbo/dubbo-nut-consumers.xml")
public class NutWebConfiguration {
}
