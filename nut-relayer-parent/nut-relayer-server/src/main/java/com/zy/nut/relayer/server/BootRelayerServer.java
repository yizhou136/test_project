package com.zy.nut.relayer.server;

import com.zy.nut.relayer.common.container.RelayerServerContainer;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;

import java.net.URL;

/**
 * Created by Administrator on 2016/11/6.
 */
//@Configuration
public class BootRelayerServer {

    public static void main(String argv[]){
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
