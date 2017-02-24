package com.zy.nut.services;


import com.zy.nut.common.msp.MspService;

import com.zy.nut.services.configuration.datasource.NutDataSourceConfiguration;
import com.zy.nut.services.configuration.mybatis.MybatisConfiguration;
import com.zy.nut.services.configuration.tx.MyProxyTransactionManagementConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.transaction.annotation.EnableTransactionManagement;

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
public class BootNutServices {
    private static  final Logger logger = LoggerFactory.getLogger(BootNutServices.class);

    public static void main(String args[]){
        SpringApplication springApplication = new SpringApplication(BootNutServices.class);
        springApplication.addListeners(new ApplicationListener<ContextRefreshedEvent>() {
            @Override
            public void onApplicationEvent(final ContextRefreshedEvent event) {
                logger.debug("ContextRefreshedEvent {}  and goto sendTo sleep",event);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        logger.debug("sendTo weakup");
                        MspService mspService = (MspService) event.getApplicationContext().getBean("mspService");
                        mspService.sendTo(1, "haha".getBytes());
                        logger.debug("mspService send to  1 haha");
                    }
                }).start();
            }
        });
        springApplication.run(args);

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}