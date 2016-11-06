package com.zy.nut.relayer.common.remoting.exchange.codec;

import com.zy.nut.relayer.common.remoting.exchange.TransfredData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.util.List;

/**
 * Created by Administrator on 2016/11/6.
 */
public class RelayerCodecHandler extends ByteToMessageCodec<Object>{

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {

    }
}
