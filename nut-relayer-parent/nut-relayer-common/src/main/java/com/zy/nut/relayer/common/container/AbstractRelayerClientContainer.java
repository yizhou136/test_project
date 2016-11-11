package com.zy.nut.relayer.common.container;


import com.zy.nut.relayer.common.configure.Configuration;
import com.zy.nut.relayer.common.remoting.RemotingException;
import com.zy.nut.relayer.common.transporter.netty.NettyClient;
import com.zy.nut.relayer.common.transporter.netty.RelayerServerClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by Administrator on 2016/11/7.
 */
public abstract class AbstractRelayerClientContainer {
    //private Set<NettyClient> clusterGroupServerClients;
    protected Set<NettyClient> serverClients;
    private volatile int successedConnections;

    protected Bootstrap bootstrap;
    protected NioEventLoopGroup nioEventLoopGroup;


    public AbstractRelayerClientContainer(Configuration configuration) throws Throwable{
        serverClients = new LinkedHashSet<NettyClient>();
        nioEventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(nioEventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, Boolean.valueOf(true))
                .option(ChannelOption.SO_KEEPALIVE, Boolean.valueOf(true))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, getConnectTimeout())
                .option(ChannelOption.AUTO_READ, Boolean.valueOf(true))
                .handler(new RelayerServerClientInitializer(null));

        init(configuration);
        nioEventLoopGroup.submit(new ElectingClusterLeaders());
    }

    public abstract void init(Configuration configuration) throws RemotingException;

    public int getConnectTimeout() {
        return 3000;
    }

    private class ServerClientConnectionFutureListener implements ChannelFutureListener{
        public void operationComplete(ChannelFuture future) throws Exception {
            if (future.isSuccess()){
                successedConnections++;
            }else if(future.cause() != null){
                future.cause().printStackTrace();
            }
        }
    }

    private class ElectingClusterLeaders implements Runnable{
        public void run() {
            if (successedConnections == 0){

            }else {

            }
        }
    }
}
