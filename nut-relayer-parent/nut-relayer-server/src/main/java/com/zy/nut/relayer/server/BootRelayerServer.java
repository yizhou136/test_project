package com.zy.nut.relayer.server;

import com.zy.nut.common.msp.MsBackService;
import com.zy.nut.common.msp.Response;
import com.zy.nut.relayer.common.beans.RoomMsg;
import com.zy.nut.relayer.common.container.RelayerServerContainer;
import com.zy.nut.relayer.server.configuration.datasource.NutDataSourceConfiguration;
import com.zy.nut.relayer.server.configuration.jpa.JpaConfiguration;
import com.zy.nut.relayer.server.configuration.mybatis.MybatisConfiguration;
import com.zy.nut.relayer.server.configuration.tx.MyProxyTransactionManagementConfiguration;
import com.zy.nut.relayer.server.service.MsgService;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.net.URL;

/**
 * Created by Administrator on 2016/11/6.
 */
//@Configuration
@SpringBootApplication(exclude = {
        DataSourceAutoConfiguration.class,
        MybatisAutoConfiguration.class,
        NutDataSourceConfiguration.class,
        JpaConfiguration.class,
        MybatisConfiguration.class,
        MyProxyTransactionManagementConfiguration.class,
        })
//@EnableJpaRepositories("com.zy.nut.relayer.common.beans.*")
//@ComponentScan(basePackages = { "com.zy.nut.relayer.common.beans.*" })
//@EntityScan("com.zy.nut.relayer.common.beans.*")
public class BootRelayerServer {
    private static  final Logger logger = LoggerFactory.getLogger(BootRelayerServer.class);

    public static void main(String args[]){
        //SpringApplication.run(BootRelayerServer.class, args);
        SpringApplication springApplication = new SpringApplication(BootRelayerServer.class);
        springApplication.addListeners(new ApplicationListener<ContextRefreshedEvent>() {
            @Override
            public void onApplicationEvent(ContextRefreshedEvent event) {
                logger.debug("ContextRefreshedEvent {}  and goto notify sleep",event);
                if (true)
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        logger.debug("notify weakup");
                        MsBackService msBackService = (MsBackService)
                                event.getApplicationContext().getBean("msBackService");
                        logger.debug("ContextRefreshedEvent {}  msBackService:{}",event, msBackService);
                        for (int i=0;i<2;i++) {
                            Response response = msBackService.nofity("goodboy".getBytes());
                            logger.debug("msBackService notify response:{}", response);
                        }
                    }
                }).start();
            }
        });
        springApplication.run(args);
    }

    public static void main2(String argv[]){
        //SpringApplication.run(BootRelayerServer.class);
        String path = "relayer.properties";
        if (argv.length > 0)
            path = argv[0];
        System.out.println("read path:"+path);
        URL url = RelayerServerContainer.class.getClassLoader().getResource(path);
        RelayerServerContainer relayerContainer = null;
        try {
            relayerContainer = new RelayerServerContainer(url);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println(relayerContainer.getConfiguration());
    }
}
