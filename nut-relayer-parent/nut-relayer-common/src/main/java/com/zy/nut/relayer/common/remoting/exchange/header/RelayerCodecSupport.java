package com.zy.nut.relayer.common.remoting.exchange.header;

import com.zy.nut.relayer.common.remoting.buffer.ChannelBuffer;
import com.zy.nut.relayer.common.remoting.buffer.ChannelBuffers;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.util.ReferenceCountUtil;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by Administrator on 2016/11/12.
 */
public class RelayerCodecSupport {
    private RelayerCodec relayerCodec;

    public RelayerCodecSupport(){
        relayerCodec = new RelayerCodec();
    }

    public byte[] encode(Object obj){
        ByteBuf byteBuf = encodeToByteBuf(obj);
        byte[] bytes = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(bytes);
        return bytes;
    }

    public ByteBuf encodeToByteBuf(Object obj){
        try {
            ChannelBuffer channelBuffer = ChannelBuffers.buffer(256);
            relayerCodec.encode(null, channelBuffer, obj);
            return  channelBuffer.toByteBuf();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object decode(byte[] data){
        try{
            ChannelBuffer channelBuffer = ChannelBuffers.buffer(256);
            channelBuffer.writeBytes(data);
            Object object =  relayerCodec.decode(null, channelBuffer);
            channelBuffer.toByteBuf().release();
            return object;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    protected final ByteBuf newDirectBuffer(ByteBuf buf) {
        final int readableBytes = buf.readableBytes();
        if (readableBytes == 0) {
            ReferenceCountUtil.safeRelease(buf);
            return Unpooled.EMPTY_BUFFER;
        }

        final ByteBufAllocator alloc = ByteBufAllocator.DEFAULT;
        if (alloc.isDirectBufferPooled()) {
            ByteBuf directBuf = alloc.directBuffer(readableBytes);
            directBuf.writeBytes(buf, buf.readerIndex(), readableBytes);
            ReferenceCountUtil.safeRelease(buf);
            return directBuf;
        }

        final ByteBuf directBuf = ByteBufUtil.threadLocalDirectBuffer();
        if (directBuf != null) {
            directBuf.writeBytes(buf, buf.readerIndex(), readableBytes);
            ReferenceCountUtil.safeRelease(buf);
            return directBuf;
        }

        // Allocating and deallocating an unpooled direct buffer is very expensive; give up.
        return buf;
    }
}
