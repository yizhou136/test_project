package com.zy.nut.relayer.common.transporter.protocol;

import com.zy.nut.relayer.common.transporter.ChannelInitializerRegister;
import io.netty.channel.ChannelHandlerContext;

import java.nio.ByteBuffer;

/**
 * Created by Administrator on 2016/12/4.
 */
public class WebsocketHandlerInitializer implements ChannelInitializerRegister{
    public boolean isSupport(ByteBuffer byteBuffer) {
        return false;
    }

    public void registerInitializer(ChannelHandlerContext ctx) {

    }
}
