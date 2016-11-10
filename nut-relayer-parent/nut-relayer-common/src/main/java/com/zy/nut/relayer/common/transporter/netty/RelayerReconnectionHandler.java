package com.zy.nut.relayer.common.transporter.netty;

import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import io.netty.channel.*;

/**
 * Created by zhougb on 2016/11/8.
 */
public class RelayerReconnectionHandler extends ChannelDuplexHandler {
    private static final Logger logger = LoggerFactory.getLogger(RelayerReconnectionHandler.class);
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("xxxxxxxxxxxxxxxxxx", cause);
        cause.printStackTrace();
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void close(ChannelHandlerContext ctx, ChannelPromise future) throws Exception {
        logger.info("xxxxxxxxxxxxxxxxxx"+future);
        super.close(ctx, future);
    }
}
