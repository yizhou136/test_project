package com.zy.nut.relayer.common.remoting;

import com.zy.nut.relayer.common.remoting.buffer.ChannelBuffer;

import java.io.IOException;

/**
 * Created by Administrator on 2016/11/6.
 */
public interface Codec {
    public static final ThreadLocal<Long> ReceiveDataStartMS = new ThreadLocal<Long>(){
        @Override
        protected Long initialValue() {
            return Long.valueOf(0);
        }
    };

    public static final ThreadLocal<Long> StartDecodeMS = new ThreadLocal<Long>(){
        @Override
        protected Long initialValue() {
            return Long.valueOf(0);
        }
    };

    void encode(Channel channel, ChannelBuffer buffer, Object message) throws IOException;


    Object decode(Channel channel, ChannelBuffer buffer) throws IOException;


    enum DecodeResult {
        NEED_MORE_INPUT, SKIP_SOME_INPUT
    }
}
