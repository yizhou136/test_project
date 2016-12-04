package com.zy.nut.relayer.common.transporter.netty;

import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.transporter.ChannelInitializerRegister;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.ByteBuffer;
import java.util.List;

/**
 * Created by Administrator on 2016/12/4.
 */
public class ProtocolDetectHandler extends ChannelDuplexHandler{
    private static final Logger logger = LoggerFactory.getLogger(ProtocolDetectHandler.class);

    private List<ChannelInitializerRegister> initializerRegisterList;
    public ProtocolDetectHandler(List<ChannelInitializerRegister> list){
        initializerRegisterList = list;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf) {
            ByteBuf byteBuf = (ByteBuf)msg;
            if (byteBuf.readableBytes() < 4){
                logger.error("the coming data is not enough readableBytes:"+byteBuf.readableBytes());
                return;
            }
            ByteBuffer byteBuffer = byteBuf.nioBuffer(0, 4);
            for (ChannelInitializerRegister register : initializerRegisterList) {
                if (register.isSupport(byteBuffer))
                    register.registerInitializer(ctx);
            }
        }

        ctx.fireChannelRead(msg);
    }
}
