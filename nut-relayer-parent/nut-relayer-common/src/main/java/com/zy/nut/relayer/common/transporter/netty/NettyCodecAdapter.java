/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zy.nut.relayer.common.transporter.netty;

import com.zy.nut.relayer.common.URL;
import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.remoting.ChannelHandler;
import com.zy.nut.relayer.common.remoting.Codec;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.io.IOException;
import java.util.List;

public class NettyCodecAdapter {
    private static final Logger logger = LoggerFactory.getLogger(NettyCodecAdapter.class);
    private final io.netty.channel.ChannelHandler encoder = new NettyCodecAdapter.InternalEncoder();
    private final io.netty.channel.ChannelHandler decoder = new NettyCodecAdapter.InternalDecoder();
    private final Codec codec;
    private final URL url;
    private final int bufferSize;
    private final ChannelHandler handler;

    public NettyCodecAdapter(Codec codec, URL url, ChannelHandler handler) {
        this.codec = codec;
        this.url = url;
        this.handler = handler;
        int b = url.getPositiveParameter("buffer", 8192);
        this.bufferSize = b >= 1024 && b <= 16384?b:8192;
    }

    public io.netty.channel.ChannelHandler getEncoder() {
        return this.encoder;
    }

    public io.netty.channel.ChannelHandler getDecoder() {
        return this.decoder;
    }

    private class InternalDecoder extends ByteToMessageDecoder {
        private InternalDecoder() {
        }

        protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
            NettyCodecAdapter.logger.info("decode xxxxxxxxxx  in:" + in + "   in.readableBytes:" + in.readableBytes() + " w:" + in.writableBytes());
            Netty4BackedChannelBuffer message = new Netty4BackedChannelBuffer(in);
            NettyCodecAdapter.logger.info("decode xxxxxxxxxx  message:" + message);
            NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), NettyCodecAdapter.this.url, NettyCodecAdapter.this.handler);

            while(true) {
                try {
                    int saveReaderIndex = message.readerIndex();

                    Object msg;
                    try {
                        msg = NettyCodecAdapter.this.codec.decode(channel, message);
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

        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
        }
    }

    @io.netty.channel.ChannelHandler.Sharable
    private class InternalEncoder extends ChannelOutboundHandlerAdapter {
        private InternalEncoder() {
        }

        public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
            Netty4BackedChannelBuffer messageBuf = new Netty4BackedChannelBuffer();
            NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), NettyCodecAdapter.this.url, NettyCodecAdapter.this.handler);

            try {
                NettyCodecAdapter.this.codec.encode(channel, messageBuf, msg);
                ctx.writeAndFlush(((Netty4BackedChannelBuffer)messageBuf).toByteBuf());
            } catch (Exception var10) {
                var10.printStackTrace();
                throw var10;
            } finally {
                NettyChannel.removeChannelIfDisconnected(ctx.channel());
            }

        }

        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
        }
    }
}