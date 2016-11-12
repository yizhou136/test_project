package com.zy.nut.relayer.client.Container;

import com.zy.nut.relayer.common.amqp.BackendAMQPClient;
import com.zy.nut.relayer.common.configure.Configuration;
import com.zy.nut.relayer.common.configure.ConfigurationLoader;

import java.net.URL;

/**
 * Created by Administrator on 2016/11/12.
 */
public class BackendClient {

    public static void main(String argv[]) throws Exception{
        String path = "relayer.properties";
        if (argv.length > 0)
            path = argv[0];
        System.out.println("read path:"+path);
        URL url = BackendClient.class.getClassLoader().getResource(path);
        Configuration configuration = ConfigurationLoader.load(url);
        BackendAMQPClient backendAMQPClient = new BackendAMQPClient(configuration);

        backendAMQPClient.transformDataTo("haha", "1234", null);
        System.in.read();
    }
}
