package com.zy.nut.relayer.common.remoting.exchange.codec;

import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.remoting.Channel;
import com.zy.nut.relayer.common.remoting.Codec;

import com.zy.nut.relayer.common.serialize.Serialization;
import com.zy.nut.relayer.common.transporter.CodecSupport;


import java.io.IOException;

/**
 * Created by Administrator on 2016/11/6.
 */
public abstract class AbstractCodec implements Codec{
    private static Logger logger = LoggerFactory.getLogger(AbstractCodec.class);
    public static final int     MAX_DEFAULT_PAYLOAD                    = 8 * 1024 * 1024;                      // 8M

    protected Serialization getSerialization(Channel channel) {
        return CodecSupport.getDefaultSerialization();
    }

    protected Serialization getSerialization(Byte id) {
        return CodecSupport.getSerializationById(id);
    }

    protected static void checkPayload(Channel channel, long size) throws IOException {
        int payload = MAX_DEFAULT_PAYLOAD;
        /*if (channel != null && channel.getUrl() != null) {
            payload = channel.getUrl().getParameter(PAYLOAD_KEY, DEFAULT_PAYLOAD);
        }*/
        if (payload > 0 && size > payload) {
            IOException e = new IOException("Data length too large: " + size + ", max payload: " + payload + ", channel: " + channel);
            logger.error("checkPayload:",e);
            throw e;
        }
    }
}
