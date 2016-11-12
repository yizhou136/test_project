package com.zy.nut.relayer.common.transporter.netty;

import com.zy.nut.relayer.common.configure.Configuration;
import com.zy.nut.relayer.common.container.ContainerExchange;
import com.zy.nut.relayer.common.remoting.RemotingException;
import com.zy.nut.relayer.common.remoting.Server;
import com.zy.nut.relayer.common.transporter.AbstractServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

/**
 * Created by Administrator on 2016/11/5.
 */
public class NettyServer extends AbstractServer implements Server{

    //private Map<String, Channel> channels; // <ip:port, channel>
    private boolean needSSL;
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private io.netty.channel.Channel channel;

    public NettyServer(Configuration configuration, ContainerExchange containerExchange) throws RemotingException {
        super(configuration,containerExchange);
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
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new RelayerServerInitializer(this, getURL()));

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
