package com.zy.nut.relayer.server.handlers.websocket;


import com.zy.nut.relayer.common.transporter.ChannelInitializerRegister;
import com.zy.nut.relayer.common.transporter.netty.ProtocolDetectHandler;
import com.zy.nut.relayer.server.handlers.MyLoggingHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.ByteBuffer;

/**
 * Created by Administrator on 2016/2/25.
 */
@Component
public class WebsocketHandlerInitializer implements ChannelInitializerRegister {
    private static final Logger logger = LoggerFactory.getLogger(WebsocketHandlerInitializer.class);
    private static final String WEBSOCKET_PATH = "/websocket";

    @Autowired
    private WebSocketFrameHandler webSocketFrameHandler;

    @Override
    public boolean isSupport(ByteBuffer byteBuffer) {
        byte[] bytes = new byte[4];
        byteBuffer.get(bytes);
        logger.info("isSupport  bytes:{}", new String(bytes));

        if (byteBuffer.get(0) == 'G'
            || byteBuffer.get(0) == 'P')
            return true;

        return false;
    }

    @Override
    public void registerInitializer(ChannelHandlerContext ctx) {
        ChannelPipeline pipeline = ctx.pipeline();
        pipeline.addLast(new MyLoggingHandler());
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(65536));
        pipeline.addLast(new WebSocketServerCompressionHandler());
        pipeline.addLast(new WebSocketServerProtocolHandler(WEBSOCKET_PATH, null, true));
        pipeline.addLast(new WebSocketIndexPageHandler(WEBSOCKET_PATH));
        pipeline.addLast(webSocketFrameHandler);

        pipeline.remove(ProtocolDetectHandler.class);
    }
}
