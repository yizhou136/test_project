package com.zy.nut.relayer.common.transporter.netty;

import com.zy.nut.relayer.common.remoting.buffer.ChannelBuffer;
import com.zy.nut.relayer.common.remoting.buffer.ChannelBufferFactory;
import com.zy.nut.relayer.common.remoting.buffer.ChannelBuffers;
import com.zy.nut.relayer.common.utils.Assert;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * Created by Administrator on 2016/11/6.
 */
public class Netty4BackedChannelBuffer implements ChannelBuffer {
    private ByteBuf buffer;

    public ByteBuf nettyChannelBuffer() {
        return this.buffer;
    }

    public Netty4BackedChannelBuffer() {
        this.buffer = ByteBufAllocator.DEFAULT.buffer();
    }

    public Netty4BackedChannelBuffer(ByteBuf buffer) {
        Assert.notNull(buffer, "buffer == null");
        this.buffer = buffer;
    }

    public ByteBuf toByteBuf() {
        return this.buffer;
    }

    public int capacity() {
        return this.buffer.capacity();
    }

    public ChannelBuffer copy(int index, int length) {
        return new Netty4BackedChannelBuffer(this.buffer.copy(index, length));
    }

    public ChannelBufferFactory factory() {
        return Netty4BackedChannelBufferFactory.getInstance();
    }

    public byte getByte(int index) {
        return this.buffer.getByte(index);
    }

    public void getBytes(int index, byte[] dst, int dstIndex, int length) {
        this.buffer.getBytes(index, dst, dstIndex, length);
    }

    public void getBytes(int index, ByteBuffer dst) {
        this.buffer.getBytes(index, dst);
    }

    public void getBytes(int index, ChannelBuffer dst, int dstIndex, int length) {
        byte[] data = new byte[length];
        this.buffer.getBytes(index, data, 0, length);
        dst.setBytes(dstIndex, data, 0, length);
    }

    public void getBytes(int index, OutputStream dst, int length) throws IOException {
        this.buffer.getBytes(index, dst, length);
    }

    public boolean isDirect() {
        return this.buffer.isDirect();
    }

    public void setByte(int index, int value) {
        this.buffer.setByte(index, value);
    }

    public void setBytes(int index, byte[] src, int srcIndex, int length) {
        this.buffer.setBytes(index, src, srcIndex, length);
    }

    public void setBytes(int index, ByteBuffer src) {
        this.buffer.setBytes(index, src);
    }

    public void setBytes(int index, ChannelBuffer src, int srcIndex, int length) {
        byte[] data = new byte[length];
        this.buffer.getBytes(srcIndex, data, 0, length);
        this.setBytes(0, (byte[])data, index, length);
    }

    public int setBytes(int index, InputStream src, int length) throws IOException {
        return this.buffer.setBytes(index, src, length);
    }

    public ByteBuffer toByteBuffer(int index, int length) {
        /*ByteBuffer byteBuffer = ByteBuffer.allocate(length);
        this.buffer.getBytes(index, byteBuffer);
        return byteBuffer;*/
        return buffer.internalNioBuffer(index, length);
    }

    public byte[] array() {
        return this.buffer.array();
    }

    public boolean hasArray() {
        return this.buffer.hasArray();
    }

    public int arrayOffset() {
        return this.buffer.arrayOffset();
    }

    public void clear() {
        this.buffer.clear();
    }

    public ChannelBuffer copy() {
        return new Netty4BackedChannelBuffer(this.buffer.copy());
    }

    public void discardReadBytes() {
        this.buffer.discardReadBytes();
    }

    public void ensureWritableBytes(int writableBytes) {
        this.buffer.ensureWritable(writableBytes);
    }

    public void getBytes(int index, byte[] dst) {
        this.buffer.getBytes(index, dst);
    }

    public void getBytes(int index, ChannelBuffer dst) {
        this.getBytes(index, dst, dst.writableBytes());
    }

    public void getBytes(int index, ChannelBuffer dst, int length) {
        if(length > dst.writableBytes()) {
            throw new IndexOutOfBoundsException();
        } else {
            this.getBytes(index, dst, dst.writerIndex(), length);
            dst.writerIndex(dst.writerIndex() + length);
        }
    }

    public void markReaderIndex() {
        this.buffer.markReaderIndex();
    }

    public void markWriterIndex() {
        this.buffer.markWriterIndex();
    }

    public boolean readable() {
        return this.buffer.readableBytes() > 0;
    }

    public int readableBytes() {
        return this.buffer.readableBytes();
    }

    public byte readByte() {
        return this.buffer.readByte();
    }

    public void readBytes(byte[] dst) {
        this.buffer.readBytes(dst);
    }

    public void readBytes(byte[] dst, int dstIndex, int length) {
        this.buffer.readBytes(dst, dstIndex, length);
    }

    public void readBytes(ByteBuffer dst) {
        this.buffer.readBytes(dst);
    }

    public void readBytes(ChannelBuffer dst) {
        this.readBytes(dst, dst.writableBytes());
    }

    public void readBytes(ChannelBuffer dst, int length) {
        if(length > dst.writableBytes()) {
            throw new IndexOutOfBoundsException();
        } else {
            this.readBytes(dst, dst.writerIndex(), length);
            dst.writerIndex(dst.writerIndex() + length);
        }
    }

    public void readBytes(ChannelBuffer dst, int dstIndex, int length) {
        if(this.readableBytes() < length) {
            throw new IndexOutOfBoundsException();
        } else {
            byte[] data = new byte[length];
            this.buffer.readBytes(data, 0, length);
            dst.setBytes(dstIndex, data, 0, length);
        }
    }

    public ChannelBuffer readBytes(int length) {
        return new Netty4BackedChannelBuffer(this.buffer.readBytes(length));
    }

    public void resetReaderIndex() {
        this.buffer.resetReaderIndex();
    }

    public void resetWriterIndex() {
        this.buffer.resetWriterIndex();
    }

    public int readerIndex() {
        return this.buffer.readerIndex();
    }

    public void readerIndex(int readerIndex) {
        this.buffer.readerIndex(readerIndex);
    }

    public void readBytes(OutputStream dst, int length) throws IOException {
        this.buffer.readBytes(dst, length);
    }

    public void setBytes(int index, byte[] src) {
        this.buffer.setBytes(index, src);
    }

    public void setBytes(int index, ChannelBuffer src) {
        this.setBytes(index, src, src.readableBytes());
    }

    public void setBytes(int index, ChannelBuffer src, int length) {
        if(length > src.readableBytes()) {
            throw new IndexOutOfBoundsException();
        } else {
            this.setBytes(index, src, src.readerIndex(), length);
            src.readerIndex(src.readerIndex() + length);
        }
    }

    public void setIndex(int readerIndex, int writerIndex) {
        this.buffer.setIndex(readerIndex, writerIndex);
    }

    public void skipBytes(int length) {
        this.buffer.skipBytes(length);
    }

    public ByteBuffer toByteBuffer() {
        /*buffer.internalNioBuffer()
        ByteBuffer byteBuffer = ByteBuffer.allocate(this.buffer.readableBytes());
        this.buffer.readBytes(byteBuffer);
        return byteBuffer;*/
        return buffer.nioBuffer();
    }

    public boolean writable() {
        return this.buffer.writableBytes() > 0;
    }

    public int writableBytes() {
        return this.buffer.writableBytes();
    }

    public void writeByte(int value) {
        this.buffer.writeByte(value);
    }

    public void writeBytes(byte[] src) {
        this.buffer.writeBytes(src);
    }

    public void writeBytes(byte[] src, int index, int length) {
        this.buffer.writeBytes(src, index, length);
    }

    public void writeBytes(ByteBuffer src) {
        this.buffer.writeBytes(src);
    }

    public void writeBytes(ChannelBuffer src) {
        this.writeBytes(src, src.readableBytes());
    }

    public void writeBytes(ChannelBuffer src, int length) {
        if(length > src.readableBytes()) {
            throw new IndexOutOfBoundsException();
        } else {
            this.writeBytes(src, src.readerIndex(), length);
            src.readerIndex(src.readerIndex() + length);
        }
    }

    public void writeBytes(ChannelBuffer src, int srcIndex, int length) {
        byte[] data = new byte[length];
        src.getBytes(srcIndex, data, 0, length);
        this.writeBytes((byte[])data, 0, length);
    }

    public int writeBytes(InputStream src, int length) throws IOException {
        return this.buffer.writeBytes(src, length);
    }

    public int writerIndex() {
        return this.buffer.writerIndex();
    }

    public void writerIndex(int writerIndex) {
        this.buffer.writerIndex(writerIndex);
    }

    public int compareTo(ChannelBuffer o) {
        return ChannelBuffers.compare(this, o);
    }
}