package com.zy.nut.relayer.server.container;

import com.zy.nut.relayer.common.remoting.Codec;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author by zy.
 */
public class MyLoggingHandler extends ChannelInboundHandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(MyLoggingHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("channelRead msg:{}", msg);
        Codec.ReceiveDataStartMS.set(System.currentTimeMillis());
        super.channelRead(ctx, msg);
    }
}
