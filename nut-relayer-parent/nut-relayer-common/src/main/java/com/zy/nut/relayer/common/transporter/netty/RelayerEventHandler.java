package com.zy.nut.relayer.common.transporter.netty;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created by zhougb on 2016/11/8.
 */
public class RelayerEventHandler extends ChannelDuplexHandler {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                ctx.close();
            } else if (e.state() == IdleState.WRITER_IDLE) {
                //ctx.writeAndFlush(new PingMessage());
                super.userEventTriggered(ctx, evt);
            }
            return;
        }
        ctx.fireUserEventTriggered(evt);
    }
}
