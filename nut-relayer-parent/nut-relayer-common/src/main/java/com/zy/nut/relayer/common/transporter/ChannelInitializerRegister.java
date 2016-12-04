package com.zy.nut.relayer.common.transporter;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;

import java.nio.ByteBuffer;

/**
 * Created by Administrator on 2016/12/4.
 */
public interface ChannelInitializerRegister {

    boolean isSupport(ByteBuffer byteBuffer);

    void  registerInitializer(ChannelHandlerContext ctx);
}
