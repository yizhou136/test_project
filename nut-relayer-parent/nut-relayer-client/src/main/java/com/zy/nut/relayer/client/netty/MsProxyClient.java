package com.zy.nut.relayer.client.netty;

import com.zy.nut.common.beans.DialogMsg;
import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.common.beans.exchange.RelayerLogin;
import com.zy.nut.common.beans.exchange.TransformData;
import com.zy.nut.relayer.common.transporter.netty.RelayerDecoderCodecHandler;
import com.zy.nut.relayer.common.transporter.netty.RelayerEncoderCodecHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import javax.net.ssl.SSLException;

/**
 * @author by zy.
 */
public class MsProxyClient {
    private static final Logger logger = LoggerFactory.getLogger(MsProxyClient.class);
    static final boolean SSL = System.getProperty("ssl") != null;
    //static final String HOST = System.getProperty("host", "127.0.0.1");
    //static final int PORT = Integer.parseInt(System.getProperty("port", "8007"));

    private String host;
    private int port;
    private Channel channel;
    private EventLoopGroup group;

    public MsProxyClient(String host, int port){
        this.host = host;
        this.port = port;

        try {
            init();
        } catch (SSLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void init() throws SSLException, InterruptedException {
        final SslContext sslCtx;
        if (SSL) {
            sslCtx = SslContextBuilder.forClient()
                    .trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        } else {
            sslCtx = null;
        }

        // Configure the client.
        group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            if (sslCtx != null) {
                                pipeline.addLast(sslCtx.newHandler(ch.alloc(), host, port));
                            }
                            pipeline.addLast(new LoggingHandler(LogLevel.INFO));
                            //pipeline.addLast("idleStateHandler", new IdleStateHandler(20, 5, 0));
                            //pipeline.addLast("relayerEventHandler", new RelayerEventHandler());

                            pipeline.addLast(new RelayerDecoderCodecHandler());
                            pipeline.addLast(new RelayerEncoderCodecHandler());
                            pipeline.addLast(new HandleRelayerHandler());
                            //p.addLast(new EchoClientHandler());
                        }
                    });

            // Start the client.
            ChannelFuture f = b.connect(host, port).sync();
            // Wait until the connection is closed.
            channel = f.channel();

        } finally {
            // Shut down the event loop to terminate all threads.
            //group.shutdownGracefully();
        }
    }

    private Channel getChannel(){
        return channel;
    }

    public void login(long uid){
        RelayerLogin relayerLogin = new RelayerLogin();
        relayerLogin.setUid(uid);
        relayerLogin.setPid((byte)0);
        relayerLogin.setUserName("zgb");
        relayerLogin.setPassword("123456");

        getChannel().writeAndFlush(relayerLogin);
    }

    public void sendDialogMsg(long fuid, long tuid){
        long lctime = System.currentTimeMillis();
        DialogMsg dialogMsg = new DialogMsg();
        dialogMsg.setFuid(fuid);
        dialogMsg.setTuid(tuid);
        dialogMsg.setMsg("haha342343434142341234"+lctime);
        dialogMsg.setLctime(lctime);

        getChannel().writeAndFlush(dialogMsg);
        //logger.info("sendDialogMsg dialogMsg:"+dialogMsg+" uid:"+uid);
    }

    public void doTransform(){
        //String project = "cuctv.weibo";
        byte project = (byte)0;
        TransformData transformData = new TransformData();
        transformData.setProject(project);
        transformData.setExchangeType(TransformData.TRANSFORM_DATA_TYPE.FANOUT.getType());
        transformData.setData("afasdfasdf");
        getChannel().writeAndFlush(transformData);
    }

    public void stop(){
        if (group != null)
            group.shutdownGracefully();
    }

    public static void main(String argv[]){
        MsProxyClient msProxyClient = new MsProxyClient("0.0.0.0", 8383);

        msProxyClient.login(1000);
        msProxyClient.login(1001);
        msProxyClient.sendDialogMsg(1000, 1001);


        //msProxyClient.stop();
        try {
            msProxyClient.getChannel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}