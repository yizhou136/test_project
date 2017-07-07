package com.zy.nut.relayer.server;

import com.alibaba.dubbo.common.Constants;
import com.zy.nut.relayer.common.configure.Configuration;
import com.zy.nut.relayer.common.configure.ConfigurationLoader;
import com.zy.nut.relayer.common.container.RelayerServerContainer;
import com.zy.nut.relayer.server.configuration.relayer.RelayerProperties;
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
        String path = System.getProperty(Constants.DUBBO_PROPERTIES_KEY);
        if (path == null || path.length() == 0) {
            path = System.getenv(Constants.DUBBO_PROPERTIES_KEY);
            if (path == null || path.length() == 0) {
                path = Constants.DEFAULT_DUBBO_PROPERTIES;
            }
        }

        RelayerProperties relayerProperties = new RelayerProperties();
        relayerProperties.setConfigure("classpath:relayer.properties");
        Configuration configuration = ConfigurationLoader.load(relayerProperties.getConfigureUrl());
        //System.setProperty("",configuration.generateNodeName());
        System.setProperty("relayer.nodeName", configuration.generateNodeName());
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
