package com.zy.nut.relayer.common.remoting.exchange.header;

import com.zy.nut.relayer.common.remoting.buffer.ChannelBuffer;
import com.zy.nut.relayer.common.remoting.buffer.ChannelBuffers;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by Administrator on 2016/11/12.
 */
public class RelayerCodecSupport {
    private ChannelBuffer channelBuffer;
    private RelayerCodec relayerCodec;

    public RelayerCodecSupport(){
        channelBuffer = ChannelBuffers.buffer(1024);
        relayerCodec = new RelayerCodec();
    }

    public byte[] encode(Object obj){
        try {
            channelBuffer.clear();
            relayerCodec.encode(null, channelBuffer, obj);
            byte[] data = new byte[channelBuffer.readableBytes()];
            channelBuffer.readBytes(data);
            return  data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object decode(byte[] data){
        try{
            channelBuffer.clear();
            channelBuffer.writeBytes(data);
            return relayerCodec.decode(null, channelBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ChannelBuffer getChannelBuffer() {
        return channelBuffer;
    }

    public void setChannelBuffer(ChannelBuffer channelBuffer) {
        this.channelBuffer = channelBuffer;
    }
}
