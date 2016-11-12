package com.zy.nut.relayer.common.transporter.netty;

import com.zy.nut.relayer.common.Version;
import com.zy.nut.relayer.common.configure.Configuration;
import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.remoting.Channel;
import com.zy.nut.relayer.common.remoting.RemotingException;
import com.zy.nut.relayer.common.transporter.AbstractClient;
import com.zy.nut.relayer.common.utils.NetUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016/11/6.
 */
public class NettyClient extends AbstractClient {
    private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
    private Bootstrap bootstrap;
    private NioEventLoopGroup nioEventLoopGroup;
    private volatile io.netty.channel.Channel nettyChannel;
    private GenericFutureListener<ChannelFuture> connectFutureListener;
    private int doConnectCount;
    private int reconnecPeriod = 5;

    public NettyClient(Configuration configuration) throws RemotingException{
        super(configuration);

        init();
    }

    public NettyClient(Configuration configuration,
                       Bootstrap bootstrap,
                       NioEventLoopGroup nioEventLoopGroup) throws RemotingException {
        super(configuration);
        this.bootstrap = bootstrap;
        this.nioEventLoopGroup = nioEventLoopGroup;
        init();
    }

    @Override
    protected void doOpen() throws Throwable {
        if (nioEventLoopGroup == null)
            nioEventLoopGroup = new NioEventLoopGroup();
        try {
            if (bootstrap == null) {
                bootstrap = new Bootstrap();
                bootstrap.group(nioEventLoopGroup)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, Boolean.valueOf(true))
                .option(ChannelOption.SO_KEEPALIVE, Boolean.valueOf(true))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, getConnectTimeout())
                .option(ChannelOption.AUTO_READ, Boolean.valueOf(true))
                .handler(new RelayerNormalClientInitializer(getURL()));
            }
        } catch (Exception exc) {
            exc.printStackTrace();
            nioEventLoopGroup.shutdownGracefully();
            throw exc;
        }
    }

    @Override
    protected void doClose() throws Throwable {
        if (nioEventLoopGroup != null)
            nioEventLoopGroup.shutdownGracefully();
    }

    @Override
    protected void doConnect() throws Throwable {
        long start = System.currentTimeMillis();
        ChannelFuture future = bootstrap.connect(getRemoteAddress());
        if (getConnectFutureListener() != null)
            future.addListener(getConnectFutureListener());
        try{
            boolean ret = future.awaitUninterruptibly(getConnectTimeout(), TimeUnit.MILLISECONDS);
            if (ret && future.isSuccess()) {
                io.netty.channel.Channel newChannel = future.channel();
                try {
                    // 关闭旧的连接
                    io.netty.channel.Channel oldChannel = NettyClient.this.nettyChannel; // copy reference
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
                    if (isClosed()) {
                        try {
                            if (logger.isInfoEnabled()) {
                                logger.info("Close new netty channel " + newChannel + ", because the client closed.");
                            }
                            newChannel.close();
                        } finally {
                            NettyClient.this.nettyChannel = null;
                            NettyChannel.removeChannelIfDisconnected(newChannel);
                        }
                    } else {
                        NettyClient.this.nettyChannel = newChannel;
                    }
                }
            } else if (future.cause() != null) {
                /*RemotingException remotingException = new RemotingException(getChannel(),
                        + getRemoteAddress() + ", error message is:" + future.cause().getMessage(), future.cause());*/
                nioEventLoopGroup.schedule(new Runnable() {
                    public void run() {
                        try {
                            NettyClient.this.doConnect();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    }
                }, reconnecPeriod, TimeUnit.SECONDS);
                doConnectCount++;
                reconnecPeriod *= doConnectCount;
                reconnecPeriod = Math.min(reconnecPeriod, 60);
                logger.error("client(" + getServerAddress() + ") failed to connect to server:"+getRemoteAddress()+
                    "\n reconnect at after "+reconnecPeriod+" second");
            } else {
                throw new RemotingException(getChannel(), "client(url: " + getServerAddress() + ") failed to connect to server "
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
        io.netty.channel.Channel c = this.nettyChannel;
        return c != null && c.isOpen()?NettyChannel.getOrAddChannel(c, getURL()):null;
    }

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    public void setBootstrap(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    public NioEventLoopGroup getNioEventLoopGroup() {
        return nioEventLoopGroup;
    }

    public void setNioEventLoopGroup(NioEventLoopGroup nioEventLoopGroup) {
        this.nioEventLoopGroup = nioEventLoopGroup;
    }

    public GenericFutureListener<ChannelFuture> getConnectFutureListener() {
        return connectFutureListener;
    }

    public void setConnectFutureListener(GenericFutureListener<ChannelFuture> connectFutureListener) {
        this.connectFutureListener = connectFutureListener;
    }
}
