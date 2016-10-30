package com.zy.nut.services;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by Administrator on 2016/10/30.
 */
@SpringBootApplication
//@EnableTransactionManagement
public class BootNutServices {

    public static void main(String args[]){
        SpringApplication.run(BootNutServices.class, args);
    }
}