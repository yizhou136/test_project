package com.zy.nut.relayer.server.test;

import com.zy.nut.relayer.common.URL;
import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.remoting.Channel;
import com.zy.nut.relayer.common.remoting.ChannelHandler;
import com.zy.nut.relayer.common.remoting.RemotingException;
import com.zy.nut.relayer.common.utils.UrlUtils;
import com.zy.nut.relayer.server.transporter.netty.NettyServer;

/**
 * Created by Administrator on 2016/11/6.
 */
public class TestRelayerServer {
    private static final Logger logger = LoggerFactory.getLogger(TestRelayerServer.class);

    public static void main(String argv[]){
        try {
            URL url = UrlUtils.parseURL("127.0.0.1:8282",null);
            NettyServer nettyServer = new NettyServer(url, new ChannelHandler() {
                public void connected(Channel channel) throws RemotingException {
                    logger.debug("connected channel: "+channel);
                }

                public void disconnected(Channel channel) throws RemotingException {
                    logger.debug("disconnected channel: "+channel);
                }

                public void sent(Channel channel, Object message) throws RemotingException {
                    logger.debug("sent channel: "+channel+" message:"+message);
                }

                public void received(Channel channel, Object message) throws RemotingException {
                    logger.debug("received channel: "+channel+" message:"+message);
                }

                public void caught(Channel channel, Throwable exception) throws RemotingException {
                    exception.printStackTrace();
                    logger.debug("caught channel: "+channel);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
