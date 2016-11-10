package com.zy.nut.relayer.common.transporter.netty;

import com.zy.nut.relayer.common.URL;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created by zhougb on 2016/11/8.
 */
public class RelayerServerInitializer extends AbstractChannelInitializer{

    public RelayerServerInitializer(URL url){
        super(url);
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast(new LoggingHandler(LogLevel.INFO));
        //pipeline.addLast("idleStateHandler", new IdleStateHandler(20, 10, 0));
        //pipeline.addLast("relayerEventHandler", new RelayerEventHandler());
        pipeline.addLast("relayerCodec", new RelayerCodecHandler(url));
        pipeline.addLast("frontClient", new HandleFrontClientHandler());
    }
}
