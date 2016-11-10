package com.zy.nut.relayer.server.test;

import com.zy.nut.relayer.common.configure.Configuration;
import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.transporter.netty.NettyServer;

/**
 * Created by Administrator on 2016/11/6.
 */
public class TestRelayerServer {
    private static final Logger logger = LoggerFactory.getLogger(TestRelayerServer.class);

    public static void main(String argv[]){
        try {
            //URL url = UrlUtils.parseURL("127.0.0.1:8282",null);
            Configuration configuration = new Configuration();
            NettyServer nettyServer = new NettyServer(configuration);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
