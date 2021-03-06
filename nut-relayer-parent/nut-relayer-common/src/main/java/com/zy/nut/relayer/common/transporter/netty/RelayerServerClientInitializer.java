package com.zy.nut.relayer.common.transporter.netty;

import com.zy.nut.relayer.common.URL;
import com.zy.nut.relayer.common.remoting.AbstractEndPoint;
import com.zy.nut.relayer.common.remoting.Server;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created by zhougb on 2016/11/8.
 */
public class RelayerServerClientInitializer extends AbstractChannelInitializer{
    private Server server;

    public RelayerServerClientInitializer(Server server, URL url){
        super(url);
        this.server = server;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new LoggingHandler(LogLevel.INFO));

        //pipeline.addLast(new RelayerReconnectionHandler());
        //pipeline.addLast("idleStateHandler", new IdleStateHandler(200, 100, 0));
        //pipeline.addLast("relayerEventHandler", new RelayerEventHandler());


        pipeline.addLast("relayerCodec", new RelayerCodecHandler(url));
        pipeline.addLast("serverClient", new RelayerServerClientHandler(server));
        //pipeline.addLast(new EchoClientHandler());

        /*NettyCodecAdapter adapter = new NettyCodecAdapter(NettyClient.this.getCodec(), NettyClient.this.getUrl(), NettyClient.this);
        pipeline.addLast("decoder", adapter.getDecoder());
        pipeline.addLast("encoder", adapter.getEncoder());*/
        //pipeline.addLast(new RelayerCodecHandler());
        //pipeline.addLast(nettyHandler);
    }
}
