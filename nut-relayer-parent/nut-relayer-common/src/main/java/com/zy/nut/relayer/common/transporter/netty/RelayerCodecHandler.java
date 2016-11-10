package com.zy.nut.relayer.common.transporter.netty;

import com.zy.nut.relayer.common.URL;
import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.remoting.Codec;
import com.zy.nut.relayer.common.remoting.exchange.TransfredData;
import com.zy.nut.relayer.common.remoting.exchange.header.RelayerCodec;
import com.zy.nut.relayer.common.transporter.CodecSupport;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016/11/6.
 */
public class RelayerCodecHandler extends ByteToMessageCodec<Object>{
    private static final Logger logger = LoggerFactory.getLogger(RelayerCodecHandler.class);

    private Codec   codec;
    private URL     url;

    public RelayerCodecHandler(URL url){
        codec = new RelayerCodec();
        this.url = url;
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        logger.info("decode xxxxxxxxxx  in:" + in + "   in.readableBytes:" + in.readableBytes() + " w:" + in.writableBytes());
        Netty4BackedChannelBuffer message = new Netty4BackedChannelBuffer(in);
        logger.info("decode xxxxxxxxxx  message:" + message);
        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), url);

        while(true) {
            try {
                int saveReaderIndex = message.readerIndex();

                Object msg;
                try {
                    msg = codec.decode(channel, message);
                } catch (IOException var12) {
                    throw var12;
                }

                if(msg == Codec.DecodeResult.NEED_MORE_INPUT) {
                    message.readerIndex(saveReaderIndex);
                } else {
                    if(saveReaderIndex == message.readerIndex()) {
                        throw new IOException("Decode without read data.");
                    }

                    if(msg != null) {
                        out.add(msg);
                    }

                    if(message.readable()) {
                        continue;
                    }
                }
            } finally {
                NettyChannel.removeChannelIfDisconnected(ctx.channel());
            }

            return;
        }
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        Netty4BackedChannelBuffer messageBuf = new Netty4BackedChannelBuffer();
        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), url);

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
