package com.zy.nut.relayer.common.transporter.netty;

import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.remoting.Codec;
import com.zy.nut.relayer.common.remoting.exchange.header.RelayerCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.IOException;
import java.util.List;

/**
 * Created by zhougb on 2016/12/6.
 */
public class RelayerDecoderCodecHandler extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger(RelayerCodecHandler.class);


    private Codec codec;

    public RelayerDecoderCodecHandler(){
        codec = new RelayerCodec();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        logger.info("decode xxxxxxxxxx  in:" + in + "   in.readableBytes:" + in.readableBytes() + " w:" + in.writableBytes());
        Netty4BackedChannelBuffer message = new Netty4BackedChannelBuffer(in);
        logger.info("decode xxxxxxxxxx  message:" + message);
        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), null);

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
}
