package com.zy.nut.relayer.client.netty;

import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.remoting.Codec;
import com.zy.nut.relayer.common.remoting.exchange.header.RelayerCodec;
import com.zy.nut.relayer.common.transporter.netty.Netty4BackedChannelBuffer;
import com.zy.nut.relayer.common.transporter.netty.NettyChannel;
import com.zy.nut.relayer.common.transporter.netty.RelayerCodecHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

import java.io.IOException;
import java.util.List;

/**
 * Created by zhougb on 2016/2/6.
 */
public class ClientRelayerDecoderCodecHandler extends ByteToMessageDecoder {
    private static final Logger logger = LoggerFactory.getLogger(RelayerCodecHandler.class);


    private Codec codec;

    public ClientRelayerDecoderCodecHandler(){
        codec = new RelayerCodec();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //logger.info("ClientRelayerDecoderCodecHandler  received msg ReceiveDataStartMS:"+Codec.ReceiveDataStartMS.get());
        Netty4BackedChannelBuffer message = new Netty4BackedChannelBuffer(in);
        Channel nettyChannel = ctx.channel();
        NettyChannel channel = NettyChannel.getOrAddChannel(nettyChannel, null);

        while(true) {
            try {
                int saveReaderIndex = message.readerIndex();
                Object msg;
                try {
                    if (Codec.StartDecodeMS.get() == 0)
                        Codec.StartDecodeMS.set(System.currentTimeMillis());
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
