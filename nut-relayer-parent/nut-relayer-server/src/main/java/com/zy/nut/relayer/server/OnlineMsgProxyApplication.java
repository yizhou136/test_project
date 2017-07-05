package com.zy.nut.relayer.server;

import com.zy.nut.relayer.common.container.RelayerServerContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import java.net.URL;

/**
 * Created by Administrator on 2016/11/6.
 */

@SpringBootApplication
//@EnableJpaRepositories("com.zy.nut.relayer.common.beans.*")
//@ComponentScan(basePackages = { "com.zy.nut.relayer.common.beans.*" })
//@EntityScan("com.zy.nut.relayer.common.beans.*")
public class OnlineMsgProxyApplication {
    private static  final Logger logger = LoggerFactory.getLogger(OnlineMsgProxyApplication.class);

    public static void main(String args[]){
        new SpringApplicationBuilder(OnlineMsgProxyApplication.class).web(false).run();
    }

    public static void main2(String argv[]){
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
