package com.zy.nut.services;


import com.zy.nut.common.msp.MsProxyService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.io.IOException;

/**
 * Created by Administrator on 2016/10/30.
 */
@SpringBootApplication(exclude = {
        /*DataSourceAutoConfiguration.class,
        MybatisAutoConfiguration.class,
        NutDataSourceConfiguration.class,
        MybatisConfiguration.class,
        MyProxyTransactionManagementConfiguration.class,*/
})
//@EnableTransactionManagement
public class ShowApplication {
    private static  final Logger logger = LoggerFactory.getLogger(ShowApplication.class);

    public static void main(String args[]){
        new SpringApplicationBuilder(ShowApplication.class).web(true).run();
    }
}