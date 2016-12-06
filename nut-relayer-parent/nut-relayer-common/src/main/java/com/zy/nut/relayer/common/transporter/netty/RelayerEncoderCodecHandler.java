package com.zy.nut.relayer.common.transporter.netty;

import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.remoting.Codec;
import com.zy.nut.relayer.common.remoting.exchange.header.RelayerCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * Created by zhougb on 2016/12/6.
 */
public class RelayerEncoderCodecHandler extends ChannelOutboundHandlerAdapter{
    private static final Logger logger = LoggerFactory.getLogger(RelayerCodecHandler.class);


    private Codec codec;
    public RelayerEncoderCodecHandler(){
        codec = new RelayerCodec();
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof ByteBuf){
            /*ByteBuf buf = (ByteBuf)msg;
            ctx.writeAndFlush(buf);*/
            super.write(ctx, msg, promise);
            return;
        }
        Netty4BackedChannelBuffer messageBuf = new Netty4BackedChannelBuffer();
        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), null);

        try {
            codec.encode(channel, messageBuf, msg);
            ctx.writeAndFlush(((Netty4BackedChannelBuffer)messageBuf).toByteBuf());
        } catch (Exception var10) {
            var10.printStackTrace();
            throw var10;
        } finally {
            NettyChannel.removeChannelIfDisconnected(ctx.channel());
        }
    }
}