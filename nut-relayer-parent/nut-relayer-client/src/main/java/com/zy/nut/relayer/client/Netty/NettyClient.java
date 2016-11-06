package com.zy.nut.relayer.client.Netty;

import com.zy.nut.relayer.client.AbstractClient;
import com.zy.nut.relayer.common.URL;
import com.zy.nut.relayer.common.Version;
import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.remoting.Channel;
import com.zy.nut.relayer.common.remoting.ChannelHandler;
import com.zy.nut.relayer.common.remoting.RemotingException;
import com.zy.nut.relayer.common.remoting.exchange.codec.RelayerCodecHandler;
import com.zy.nut.relayer.common.transporter.netty.NettyChannel;
import com.zy.nut.relayer.common.transporter.netty.NettyCodecAdapter;
import com.zy.nut.relayer.common.transporter.netty.NettyHandler;
import com.zy.nut.relayer.common.utils.NetUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;


import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/11/6.
 */
public class NettyClient extends AbstractClient{
    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    private Bootstrap bootstrap;
    private boolean serverSide;
    private volatile io.netty.channel.Channel channel;

    public NettyClient(URL url, final ChannelHandler handler) throws RemotingException {
        super(url,handler);
    }

    public NettyClient(URL url, final ChannelHandler handler, boolean serverSide) throws RemotingException {
        super(url,handler);
        this.serverSide = serverSide;
    }

    @Override
    protected void doOpen() throws Throwable {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            final NettyHandler nettyHandler = new NettyHandler(this.getUrl(), this);
            this.bootstrap = new Bootstrap();
            this.bootstrap.group(group)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY, Boolean.valueOf(true))
            .option(ChannelOption.SO_KEEPALIVE, Boolean.valueOf(true))
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, Integer.valueOf(this.getTimeout()))
            .option(ChannelOption.AUTO_READ, Boolean.valueOf(true))
            .handler(new ChannelInitializer() {
                @Override
                protected void initChannel(io.netty.channel.Channel channel) throws Exception {
                    ChannelPipeline pipeline = channel.pipeline();
                    //pipeline.addLast(new io.netty.channel.ChannelHandler[]{new LoggingHandler(LogLevel.INFO)});
                    pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                    NettyCodecAdapter adapter = new NettyCodecAdapter(NettyClient.this.getCodec(), NettyClient.this.getUrl(), NettyClient.this);
                    pipeline.addLast("decoder", adapter.getDecoder());
                    pipeline.addLast("encoder", adapter.getEncoder());
                    //pipeline.addLast(new RelayerCodecHandler());
                    pipeline.addLast(nettyHandler);
                }
            });
        } catch (Exception var3) {
            var3.printStackTrace();
            group.shutdownGracefully();
        }
    }


    @Override
    protected void doClose() throws Throwable {

    }

    @Override
    protected void doConnect() throws Throwable {
        long start = System.currentTimeMillis();
        ChannelFuture future = bootstrap.connect(getConnectAddress());
        try{
            boolean ret = future.awaitUninterruptibly(getConnectTimeout(), TimeUnit.MILLISECONDS);

            if (ret && future.isSuccess()) {
                io.netty.channel.Channel newChannel = future.channel();
                //newChannel.setInterestOps(Channel.OP_READ_WRITE);
                try {
                    // 关闭旧的连接
                    io.netty.channel.Channel oldChannel = NettyClient.this.channel; // copy reference
                    if (oldChannel != null) {
                        try {
                            if (logger.isInfoEnabled()) {
                                logger.info("Close old netty channel " + oldChannel + " on create new netty channel " + newChannel);
                            }
                            oldChannel.close();
                        } finally {
                            NettyChannel.removeChannelIfDisconnected(oldChannel);
                        }
                    }
                } finally {
                    if (NettyClient.this.isClosed()) {
                        try {
                            if (logger.isInfoEnabled()) {
                                logger.info("Close new netty channel " + newChannel + ", because the client closed.");
                            }
                            newChannel.close();
                        } finally {
                            NettyClient.this.channel = null;
                            NettyChannel.removeChannelIfDisconnected(newChannel);
                        }
                    } else {
                        NettyClient.this.channel = newChannel;
                    }
                }
            } else if (future.cause() != null) {
                throw new RemotingException(getChannel(), "client(url: " + getUrl() + ") failed to connect to server "
                        + getRemoteAddress() + ", error message is:" + future.cause().getMessage(), future.cause());
            } else {
                throw new RemotingException(getChannel(), "client(url: " + getUrl() + ") failed to connect to server "
                        + getRemoteAddress() + " client-side timeout "
                        + getConnectTimeout() + "ms (elapsed: " + (System.currentTimeMillis() - start) + "ms) from netty client "
                        + NetUtils.getLocalHost() + " using relayer version " + Version.getVersion());
            }
        }finally{
            if (! isConnected()) {
                future.cancel(true);
            }
        }
    }

    @Override
    protected void doDisConnect() throws Throwable {
        try {
            //NettyChannel.removeChannelIfDisconnected(this.channel);
        } catch (Throwable var2) {
            logger.warn(var2.getMessage());
        }
    }

    @Override
    protected Channel getChannel() {
        io.netty.channel.Channel c = this.channel;
        return c != null && c.isOpen()?NettyChannel.getOrAddChannel(c, getUrl(), this):null;
    }
}
