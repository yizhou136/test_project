package com.zy.nut.relayer.common.container;


import com.zy.nut.relayer.common.configure.Configuration;
import com.zy.nut.relayer.common.remoting.RemotingException;
import com.zy.nut.relayer.common.remoting.Server;
import com.zy.nut.relayer.common.transporter.netty.NettyClient;
import com.zy.nut.relayer.common.transporter.netty.RelayerServerClientInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2016/11/7.
 */
public abstract class AbstractRelayerClientContainer {
    //private Set<NettyClient> clusterGroupServerClients;
    private static final AtomicInteger selectServerIdx = new AtomicInteger();
    protected List<NettyClient> serverClients;
    private volatile int successedConnections;

    protected Bootstrap bootstrap;
    protected NioEventLoopGroup nioEventLoopGroup;

    protected Server server;


    public AbstractRelayerClientContainer(Server server,Configuration configuration) throws Throwable{
        this.server = server;
        serverClients = new LinkedList<NettyClient>();
        nioEventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(nioEventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, Boolean.valueOf(true))
                .option(ChannelOption.SO_KEEPALIVE, Boolean.valueOf(true))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, getConnectTimeout())
                .option(ChannelOption.AUTO_READ, Boolean.valueOf(true))
                .handler(new RelayerServerClientInitializer(server,null));

        init(configuration);
        nioEventLoopGroup.submit(new ElectingClusterLeaders());
    }

    public abstract void init(Configuration configuration) throws RemotingException;

    public void sendDataToLeadingServers(Object msg, boolean isFanout) throws RemotingException{
        if (serverClients == null || serverClients.isEmpty())
            return;
        if (isFanout) {
            for (NettyClient nettyClient : serverClients) {
                try {
                    nettyClient.send(msg, false);
                }catch (RemotingException e){
                    e.printStackTrace();
                }
            }
        }else {
            int retryCnt = serverClients.size();
            while (retryCnt > 0) {
                int idx = selectServerIdx.getAndIncrement();
                idx = idx % serverClients.size();
                NettyClient nettyClient = serverClients.get(idx);
                try {
                    nettyClient.send(msg, false);
                    break;
                } catch (RemotingException e) {
                    e.printStackTrace();
                }
                retryCnt--;
            }
        }
    }

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
