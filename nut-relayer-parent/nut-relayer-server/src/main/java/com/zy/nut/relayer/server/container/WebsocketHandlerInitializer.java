package com.zy.nut.relayer.server.container;

import com.zy.nut.relayer.common.transporter.ChannelInitializerRegister;
import io.netty.channel.ChannelHandlerContext;

import java.nio.ByteBuffer;

/**
 * Created by Administrator on 2017/2/25.
 */
public class WebsocketHandlerInitializer implements ChannelInitializerRegister {
    @Override
    public boolean isSupport(ByteBuffer byteBuffer) {
        return false;
    }

    @Override
    public void registerInitializer(ChannelHandlerContext ctx) {

    }
}
