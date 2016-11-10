package com.zy.nut.relayer.client.test;

import com.zy.nut.relayer.common.URL;
import com.zy.nut.relayer.common.configure.Configuration;
import com.zy.nut.relayer.common.configure.ConfigurationLoader;
import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.remoting.Channel;
import com.zy.nut.relayer.common.remoting.ChannelHandler;
import com.zy.nut.relayer.common.remoting.RemotingException;
import com.zy.nut.relayer.common.remoting.exchange.TransfredData;
import com.zy.nut.relayer.common.transporter.netty.NettyClient;
import com.zy.nut.relayer.common.utils.UrlUtils;

/**
 * Created by Administrator on 2016/11/6.
 */
public class TestRelayerClient {
    private static final Logger logger = LoggerFactory.getLogger(TestRelayerClient.class);

    public static void main(String argv[]){
        try {
            java.net.URL url = TestRelayerClient.class.getClassLoader().getResource("relayer.properties");
            Configuration configuration = ConfigurationLoader.load(url);
            NettyClient nettyClient = new NettyClient(configuration);

            User user = new User();
            user.setAddreass("asdfasdfas");
            user.setAge(234);
            user.setName("asdfasd");

            TransfredData transfredData = new TransfredData();
            transfredData.setGroup("com.zy");
            transfredData.setFid("123");
            transfredData.setTid("456");
            transfredData.setData(user);

            //Thread.sleep(1000);
            nettyClient.send(transfredData, false);

            System.in.read();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
