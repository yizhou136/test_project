package com.zy.nut.relayer.common.transporter.netty;

import com.zy.nut.common.beans.DialogMsg;
import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.remoting.Codec;
import com.zy.nut.relayer.common.remoting.exchange.header.RelayerCodec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

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
        //if (Codec.ReceiveDataStartMS.get() == 0)

        logger.info("RelayerDecoderCodecHandler  received msg start:"+Codec.ReceiveDataStartMS.get()+" in:"+in);
        Netty4BackedChannelBuffer message = new Netty4BackedChannelBuffer(in);
        Channel nettyChannel = ctx.channel();
        NettyChannel channel = NettyChannel.getOrAddChannel(nettyChannel, null);

        long startDecode = 0;
        long endDecode = 0;
        while(true) {
            try {
                int saveReaderIndex = message.readerIndex();
                Object msg;
                try {
                    //Codec.StartDecodeMS.set(System.currentTimeMillis());
                    startDecode = System.currentTimeMillis();
                    msg = codec.decode(channel, message);
                    endDecode = System.currentTimeMillis();
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
                        if (msg instanceof DialogMsg){
                            DialogMsg dialogMsg = (DialogMsg)msg;
                            dialogMsg.setProxyReceiveMs(Codec.ReceiveDataStartMS.get());
                            //dialogMsg.setProxyReceiveBackMs(receiveMs);
                            dialogMsg.setStartDecodeMs(startDecode);
                            dialogMsg.setEndDecodeMs(endDecode);
                        }
                    }

                    if(message.readable()) {
                        continue;
                    }
                }
            } finally {
                NettyChannel.removeChannelIfDisconnected(ctx.channel());
            }

            //Codec.ReceiveDataStartMS.set(0L);
            return;
        }
    }
}
