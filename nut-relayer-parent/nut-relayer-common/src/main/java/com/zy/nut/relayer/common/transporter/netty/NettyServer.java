package com.zy.nut.relayer.common.transporter.netty;


import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.zy.nut.common.beans.DialogMsg;
import com.zy.nut.common.beans.NodeServer;
import com.zy.nut.common.beans.RoomMsg;
import com.zy.nut.relayer.common.configure.Configuration;
import com.zy.nut.relayer.common.container.ContainerExchange;
import com.zy.nut.relayer.common.remoting.RemotingException;
import com.zy.nut.relayer.common.remoting.Server;
import com.zy.nut.common.beans.exchange.RelayerEnterRoom;
import com.zy.nut.common.beans.exchange.RelayerLeftRoom;
import com.zy.nut.common.beans.exchange.RelayerLogin;
import com.zy.nut.common.beans.exchange.RelayerLogout;
import com.zy.nut.relayer.common.transporter.AbstractServer;
import com.zy.nut.relayer.common.transporter.ChannelInitializerRegister;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Administrator on 2016/11/5.
 */
public class NettyServer extends AbstractServer implements Server{
    public static final AttributeKey<Boolean> IS_WEBSOCKET_CHANNEL = AttributeKey.valueOf("IS_WEBSOCKET_CHANNEL");

    private NettyClient parentNode;
    private ConcurrentHashMap<String, NodeServer> childrenNodesMap;

    private ConcurrentHashMap<Long, ConcurrentHashSet<Channel>> userLoginedChannelMap;
    private ConcurrentHashMap<Long, ConcurrentHashSet<Channel>> roomEnteredChannelMap;
    private AtomicInteger totalChannelCounter;

    private boolean needSSL;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private Channel channel;



    public NettyServer(Configuration configuration, ContainerExchange containerExchange, List<ChannelInitializerRegister> initializerRegisterList) throws RemotingException {
        super(configuration,containerExchange, initializerRegisterList);
        userLoginedChannelMap = new ConcurrentHashMap<>(1000);
        roomEnteredChannelMap = new ConcurrentHashMap<>(100);
        childrenNodesMap = new ConcurrentHashMap<>(20);
        totalChannelCounter = new AtomicInteger();
    }

    @Override
    protected void doOpen() throws Throwable {
        // Configure SSL.
        final SslContext sslCtx;
        if (needSSL) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
        } else {
            sslCtx = null;
        }

        // Configure the server.
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        try {
            //this.channels = nettyHandler.getChannels();
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    //.childHandler(new ProtocolDetectHandler(initializerRegisterList));
                    .childHandler(new RelayerServerInitializer(initializerRegisterList));

            // Start the server.
            ChannelFuture f = b.bind(getBindAddress()).sync();
            channel = f.channel();
            // Wait until the server socket is closed.
            //f.channel().closeFuture().sync();
        } catch (Exception e){
            e.printStackTrace();
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            throw  e;
        }
    }

    @Override
    protected void doClose() throws Throwable {
        if (bossGroup != null){
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null){
            workerGroup.shutdownGracefully();
        }
    }

    @Override
    public void heartbeat() {
    }

    @Override
    public void userLogin(RelayerLogin relayerLogin) {
        Long uid = relayerLogin.getUid();
        userLoginedChannelMap.compute(uid, (key, val)->{
            if (val == null){
                //val = new ConcurrentLinkedQueue<Channel>();
                val = new ConcurrentHashSet<Channel>();
            }

            val.add(relayerLogin.getChannel());
            return val;
        });
    }

    @Override
    public void userLogout(RelayerLogout relayerLogout) {
        Long uid = relayerLogout.getUid();
        userLoginedChannelMap.compute(uid, (key, val)->{
            if (val == null){
                logger.warn("userLogout but the user channel is empty");
            }else
                val.remove(relayerLogout.getChannel());

            return val;
        });
    }

    @Override
    public void enterRoom(RelayerEnterRoom relayerEnterRoom) {
        Long rid = relayerEnterRoom.getRid();
        roomEnteredChannelMap.compute(rid, (key, val)->{
            if (val == null){
                val = new ConcurrentHashSet<Channel>();
            }

            val.add(relayerEnterRoom.getChannel());
            return val;
        });
    }

    @Override
    public void leftRoom(RelayerLeftRoom relayerLeftRoom) {
        Long rid = relayerLeftRoom.getRid();
        roomEnteredChannelMap.compute(rid, (key, val)->{
            if (val == null){
                logger.warn("leftRoom but the room of user channel is empty");
            }else
                val.remove(relayerLeftRoom.getChannel());

            return val;
        });

    }

    @Override
    public void sendTo(DialogMsg dialogMsg) {
        Long fuid = dialogMsg.getFuid();
        Long tuid = dialogMsg.getTuid();
        //Queue<Channel> queue = userLoginedChannelMap.get(tuid);
        Set<Channel> queue = userLoginedChannelMap.get(tuid);
        if (queue != null)
            for (Channel channel : queue){
                Attribute<Boolean> attribute = channel.attr(IS_WEBSOCKET_CHANNEL);
                if (attribute.get() != null
                        && attribute.get()) {
                    String msg = String.format("%d => %d: %s",
                            fuid, tuid, dialogMsg.getMsg());
                    channel.writeAndFlush(new TextWebSocketFrame(msg));
                }else {
                    channel.writeAndFlush(dialogMsg);
                }
            }

        logger.info("sentTo fuid:{} tuid:{}",
                fuid, tuid);
    }

    @Override
    public void publish(RoomMsg roomMsg) {
        Long rid = roomMsg.getRid();
        Set<Channel> queue = roomEnteredChannelMap.get(rid);
        if (queue != null)
            for (Channel channel : queue){
                channel.writeAndFlush(roomMsg);
            }

        logger.info("publish rid:{} roomMsg:{}",
                rid, roomMsg);
    }

    /*public Collection<Channel> getChannels() {
        Collection<Channel> chs = new HashSet<Channel>();
        for (Channel channel : this.channels.values()) {
            if (channel.isConnected()) {
                chs.add(channel);
            } else {
                channels.remove(NetUtils.toAddressString(channel.getRemoteAddress()));
            }
        }
        return chs;
    }

    public Channel getChannel(InetSocketAddress remoteAddress) {
        return channels.get(NetUtils.toAddressString(remoteAddress));
    }

    public boolean isBound() {
        return channel.isOpen();
    }*/
}
