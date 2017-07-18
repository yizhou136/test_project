package com.zy.nut.relayer.server.container;

import com.zy.nut.relayer.common.remoting.exchange.header.HeaderExchangeCodec;
import com.zy.nut.relayer.common.transporter.ChannelInitializerRegister;
import com.zy.nut.relayer.common.transporter.netty.*;
import com.zy.nut.relayer.common.utils.NamedThreadFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.MultithreadEventExecutorGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

/**
 * Created by Administrator on 2016/12/4.
 */
@Component
public class RelayerHandlerInitializer implements ChannelInitializerRegister{
    @Autowired
    private HandleRelayerHandler handleRelayerHandler;

    private EventExecutorGroup eventExecutorGroup;


    public RelayerHandlerInitializer(){
        eventExecutorGroup = new DefaultEventLoopGroup(0,
                new NamedThreadFactory("relayer_bussnes_thread_", true));
    }


    public boolean isSupport(ByteBuffer byteBuffer) {
        if (byteBuffer.get(0) == HeaderExchangeCodec.MAGIC_HIGH &&
            byteBuffer.get(1) == HeaderExchangeCodec.MAGIC_LOW)
            return true;

        return false;
    }

    public void registerInitializer(ChannelHandlerContext ctx) {
        ChannelPipeline pipeline = ctx.pipeline();
        //pipeline.addLast("relayerCodec", new RelayerCodecHandler(null));
        pipeline.addLast(new MyLoggingHandler());
        pipeline.addLast(new RelayerDecoderCodecHandler());
        pipeline.addLast(new RelayerEncoderCodecHandler());
        //pipeline.addLast(eventExecutorGroup, handleRelayerHandler);
        pipeline.addLast(handleRelayerHandler);

        pipeline.remove(ProtocolDetectHandler.class);
    }
}
