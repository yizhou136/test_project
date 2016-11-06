package com.zy.nut.relayer.common.remoting.exchange.header;

import com.zy.nut.relayer.common.io.Bytes;
import com.zy.nut.relayer.common.io.StreamUtils;
import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.remoting.Channel;
import com.zy.nut.relayer.common.remoting.Codec;
import com.zy.nut.relayer.common.remoting.buffer.ChannelBuffer;
import com.zy.nut.relayer.common.remoting.buffer.ChannelBufferInputStream;
import com.zy.nut.relayer.common.remoting.buffer.ChannelBufferOutputStream;
import com.zy.nut.relayer.common.remoting.exchange.TransfredData;
import com.zy.nut.relayer.common.remoting.exchange.codec.AbstractCodec;

import com.zy.nut.relayer.common.serialize.ObjectInput;
import com.zy.nut.relayer.common.serialize.ObjectOutput;
import com.zy.nut.relayer.common.serialize.Serialization;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/11/6.
 */
public class HeaderExchangeCodec extends AbstractCodec implements Codec{
    private static Logger logger = LoggerFactory.getLogger(AbstractCodec.class);
    protected  static final int HEADER_LENGTH          = 15;
    // magic header.
    protected static final short    MAGIC              = (short) 0xdabf;

    protected static final byte     MAGIC_HIGH         = Bytes.short2bytes(MAGIC)[0];

    protected static final byte     MAGIC_LOW          = Bytes.short2bytes(MAGIC)[1];

    protected static final byte     FLAG_EVENT     = (byte) 0x20;

    protected static final int      SERIALIZATION_MASK = 0x1f;

    public void encode(Channel channel, ChannelBuffer buffer, Object message) throws IOException {
        Serialization serialization = getSerialization(channel);
        byte [] header = new byte[HEADER_LENGTH];
        Bytes.short2bytes(MAGIC, header);
        header[2] = serialization.getContentTypeId();

        TransfredData transfredData = (TransfredData) message;
        if (transfredData.isEvent()) header[2] |= FLAG_EVENT;
        // set request id.
        Bytes.long2bytes(transfredData.getId(), header, 4);

        // encode request data.
        int savedWriteIndex = buffer.writerIndex();
        buffer.writerIndex(savedWriteIndex + HEADER_LENGTH);
        ChannelBufferOutputStream bos = new ChannelBufferOutputStream(buffer);
        ObjectOutput out = serialization.serialize(channel.getUrl(), bos);
        encodeTransfredData(channel, out, transfredData);
        out.flushBuffer();
        bos.flush();
        bos.close();
        int len = bos.writtenBytes();
        checkPayload(channel, len);
        Bytes.int2bytes(len, header, 11);

        // write
        buffer.writerIndex(savedWriteIndex);
        buffer.writeBytes(header); // write header.
        buffer.writerIndex(savedWriteIndex + HEADER_LENGTH + len);
    }

    public Object decode(Channel channel, ChannelBuffer buffer) throws IOException {
        int readable = buffer.readableBytes();
        byte[] header = new byte[Math.min(readable, HEADER_LENGTH)];
        buffer.readBytes(header);
        return decode(channel, buffer, readable, header);
    }

    protected Object decode(Channel channel, ChannelBuffer buffer, int readable, byte[] header) throws IOException {
        // check magic number.
        if (readable > 0 && header[0] != MAGIC_HIGH
                || readable > 1 && header[1] != MAGIC_LOW) {
            int length = header.length;
            if (header.length < readable) {
                header = Bytes.copyOf(header, readable);
                buffer.readBytes(header, length, readable - length);
            }
            for (int i = 1; i < header.length - 1; i ++) {
                if (header[i] == MAGIC_HIGH && header[i + 1] == MAGIC_LOW) {
                    buffer.readerIndex(buffer.readerIndex() - header.length + i);
                    //header = Bytes.copyOf(header, i);
                    break;
                }
            }
            return decode(channel, buffer);
        }
        // check length.
        if (readable < HEADER_LENGTH) {
            return DecodeResult.NEED_MORE_INPUT;
        }

        // get data length.
        int len = Bytes.bytes2int(header, 11);
        checkPayload(channel, len);

        int tt = len + HEADER_LENGTH;
        if( readable < tt ) {
            return DecodeResult.NEED_MORE_INPUT;
        }

        // limit input stream.
        ChannelBufferInputStream is = new ChannelBufferInputStream(buffer, len);

        try {
            return decodeBody(channel, is, header);
        } finally {
            if (is.available() > 0) {
                try {
                    if (logger.isWarnEnabled()) {
                        logger.warn("Skip input stream " + is.available());
                    }
                    StreamUtils.skipUnusedStream(is);
                } catch (IOException e) {
                    logger.warn(e.getMessage(), e);
                }
            }
        }
    }

    protected Object decodeBody(Channel channel, InputStream is, byte[] header) throws IOException {
        byte flag = header[2], proto = (byte) (flag & SERIALIZATION_MASK);
        Serialization s = getSerialization(proto);
        ObjectInput in = s.deserialize(channel.getUrl(), is);
        // get request id.
        long id = Bytes.bytes2long(header, 4);
        TransfredData transfredData = new TransfredData(id);

        try {
            if ((flag & FLAG_EVENT) != 0) {
                decodeTransfredData(channel, in, transfredData);
            }else {
                decodeTransfredData(channel, in, transfredData);
            }
        } catch (Throwable t) {
            // bad request
            transfredData.setBroken(true);
            transfredData.setData(t);
        }

        return transfredData;
    }

    protected void encodeTransfredData(Channel channel, ObjectOutput out, TransfredData transfredData) throws IOException{
        out.writeObject(transfredData);
    }

    protected void decodeTransfredData(Channel channel, ObjectInput in, TransfredData transfredData) throws IOException{
        try {
            transfredData.setData(in.readObject());
        } catch (ClassNotFoundException e) {
            throw new IOException("Read object failed.", e);
        }
    }
}
