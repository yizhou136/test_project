package com.zy.nut.relayer.common.transporter.netty;

import com.zy.nut.relayer.common.remoting.buffer.ChannelBuffer;
import com.zy.nut.relayer.common.remoting.buffer.ChannelBufferFactory;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.nio.ByteBuffer;

/**
 * Created by Administrator on 2016/11/6.
 */
public class Netty4BackedChannelBufferFactory implements ChannelBufferFactory {
    private static final Netty4BackedChannelBufferFactory INSTANCE = new Netty4BackedChannelBufferFactory();

    public Netty4BackedChannelBufferFactory() {
    }

    public static ChannelBufferFactory getInstance() {
        return INSTANCE;
    }

    public ChannelBuffer getBuffer() {
        return new Netty4BackedChannelBuffer();
    }

    public ChannelBuffer getBuffer(int capacity) {
        return new Netty4BackedChannelBuffer(ByteBufAllocator.DEFAULT.buffer(capacity));
    }

    public ChannelBuffer getBuffer(byte[] array, int offset, int length) {
        ByteBuf byteBuf = ByteBufAllocator.DEFAULT.buffer(length);
        Netty4BackedChannelBuffer netty4BackedChannelBuffer = new Netty4BackedChannelBuffer(byteBuf);
        netty4BackedChannelBuffer.writeBytes(array, offset, length);
        return netty4BackedChannelBuffer;
    }

    public ChannelBuffer getBuffer(ByteBuffer nioBuffer) {
        Netty4BackedChannelBuffer netty4BackedChannelBuffer = new Netty4BackedChannelBuffer(ByteBufAllocator.DEFAULT.buffer());
        netty4BackedChannelBuffer.writeBytes(nioBuffer);
        return netty4BackedChannelBuffer;
    }
}
