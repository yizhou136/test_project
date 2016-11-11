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
import com.zy.nut.relayer.common.remoting.exchange.codec.AbstractCodec;

import com.zy.nut.relayer.common.serialize.ObjectInput;
import com.zy.nut.relayer.common.serialize.ObjectOutput;
import com.zy.nut.relayer.common.serialize.Serialization;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Administrator on 2016/11/6.
 *   magic  type&serilization&other    data_length       data
 *   --          -                       ----         pingpong,loginout,electing,transfrom etc
 *
 */
public class HeaderExchangeCodec extends AbstractCodec implements Codec{
    private static Logger logger = LoggerFactory.getLogger(AbstractCodec.class);
    protected  static final int HEADER_LENGTH          = 7;
    protected  static final int DATA_LENGTH_POS        = 3;
    // magic header.
    protected static final short    MAGIC              = (short) 0xdabf;
    protected static final byte     MAGIC_HIGH         = Bytes.short2bytes(MAGIC)[0];
    protected static final byte     MAGIC_LOW          = Bytes.short2bytes(MAGIC)[1];

    protected static final byte     LOGIN_FLAG_TYPE         = (byte) 0x01;
    protected static final byte     LOGOUT_FLAG_TYPE        = (byte) 0x02;
    protected static final byte     PINGPONG_FLAG_TYPE      = (byte) 0x03;
    protected static final byte     ELECTING_FLAG_TYPE      = (byte) 0x04;
    protected static final byte     TRANSFORM_FLAG_TYPE     = (byte) 0x05;
    protected static final byte     REGISTERING_ORUNREG_FLAG_TYPE   = (byte) 0x06;

    protected static final byte      FLAG_TYPE_MASK = 0x0F;
    protected static final byte      SERIALIZATION_MASK = 0x70;
    protected static final byte      SERIALIZATION_MASK_OFFSET = 4;
    protected static final int       OTHER_MASK = 0x80;
    protected static final byte      OTHER_MASK_OFFSET = 7;

    public void encode(Channel channel, ChannelBuffer buffer, Object message) throws IOException {
        Serialization serialization = getSerialization(channel);
        byte [] header = new byte[HEADER_LENGTH];
        Bytes.short2bytes(MAGIC, header);
        byte serializationId = serialization.getContentTypeId();
        serializationId = (byte)((serializationId << SERIALIZATION_MASK_OFFSET) & SERIALIZATION_MASK);
        header[2] =  serializationId;
        // set request id.
        //Bytes.long2bytes(transfredData.getId(), header, 4);
        // encode request data.
        int savedWriteIndex = buffer.writerIndex();
        buffer.writerIndex(savedWriteIndex + HEADER_LENGTH);
        ChannelBufferOutputStream bos = new ChannelBufferOutputStream(buffer);
        ObjectOutput out = serialization.serialize(null, bos);
        byte type = encodeTransfredData(channel, out, message);
        out.flushBuffer();
        bos.flush();
        bos.close();
        header[2] |= type;
        int len = bos.writtenBytes();
        checkPayload(channel, len);
        Bytes.int2bytes(len, header, DATA_LENGTH_POS);

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
        int len = Bytes.bytes2int(header, DATA_LENGTH_POS);
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
        byte flag = header[2], proto = (byte) ((flag&SERIALIZATION_MASK)>>>SERIALIZATION_MASK_OFFSET), type = (byte)(flag & FLAG_TYPE_MASK);
        Serialization s = getSerialization(proto);
        ObjectInput in = s.deserialize(null, is);
        // get request id.
        //long id = Bytes.bytes2long(header, 4);
        Object ret = null;
        try {
            ret = decodeTransfredData(channel, in, type);
        } catch (Throwable t) {
            t.printStackTrace();
        }

        return ret;
    }

    protected byte encodeTransfredData(Channel channel, ObjectOutput out, Object msg) throws IOException{
        out.writeObject(msg);
        return 0;
    }

    protected Object decodeTransfredData(Channel channel, ObjectInput in, byte type) throws IOException{
        if (type == LOGIN_FLAG_TYPE){
        }else if (type == LOGIN_FLAG_TYPE){
        }else if (type == LOGOUT_FLAG_TYPE){
        }else if (type == ELECTING_FLAG_TYPE){
        }else if (type == TRANSFORM_FLAG_TYPE){
        }
        return null;
    }
}
