package com.zy.nut.relayer.client.test;

import com.zy.nut.relayer.client.Netty.NettyClient;
import com.zy.nut.relayer.common.URL;
import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.remoting.Channel;
import com.zy.nut.relayer.common.remoting.ChannelHandler;
import com.zy.nut.relayer.common.remoting.RemotingException;
import com.zy.nut.relayer.common.remoting.exchange.TransfredData;
import com.zy.nut.relayer.common.utils.UrlUtils;

/**
 * Created by Administrator on 2016/11/6.
 */
public class TestRelayerClient {
    private static final Logger logger = LoggerFactory.getLogger(TestRelayerClient.class);

    public static void main(String argv[]){
        try {
            URL url = UrlUtils.parseURL("127.0.0.1:8282",null);
            NettyClient nettyClient = new NettyClient(url, new ChannelHandler() {
                public void connected(Channel channel) throws RemotingException {
                    logger.info("connected channel: "+channel);
                }

                public void disconnected(Channel channel) throws RemotingException {
                    logger.info("disconnected channel: "+channel);
                }

                public void sent(Channel channel, Object message) throws RemotingException {
                    logger.info("sent channel: "+channel+" message:"+message);
                }

                public void received(Channel channel, Object message) throws RemotingException {
                    logger.info("received channel: "+channel+" message:"+message);
                }

                public void caught(Channel channel, Throwable exception) throws RemotingException {
                    exception.printStackTrace();
                    logger.error("caught channel: "+channel,exception);
                }
            });

            User user = new User();
            user.setAddreass("asdfasdfas");
            user.setAge(234);
            user.setName("asdfasd");

            TransfredData transfredData = new TransfredData();
            transfredData.setGroup("com.zy");
            transfredData.setFid("123");
            transfredData.setTid("456");
            transfredData.setData(user);
            nettyClient.send(transfredData);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
