package com.zy.nut.relayer.client.Container;

import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.remoting.buffer.ChannelBuffer;
import com.zy.nut.relayer.common.remoting.buffer.ChannelBuffers;
import com.zy.nut.relayer.common.remoting.exchange.RelayerLoginLogout;
import com.zy.nut.relayer.common.remoting.exchange.header.RelayerCodec;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zhougb on 2016/11/9.
 */
public class RelayerClient {
    private static final Logger logger = LoggerFactory.getLogger(RelayerClient.class);
    public static void main(String argv[]) throws Exception{
        RelayerCodec relayerCodec = new RelayerCodec();
        ChannelBuffer channelBuffer = ChannelBuffers.buffer(1024);
        Random random = new Random(10000);
        NioConnection.HostPortPair hostPortPair = new NioConnection.HostPortPair("127.0.0.1", 8383);
        List<NioConnection.HostPortPair> list = new ArrayList<NioConnection.HostPortPair>();
        list.add(hostPortPair);
        NioConnection nioConnection = new NioConnection(list);
        nioConnection.doConnection();


        long uid = Math.abs(random.nextLong());
        RelayerLoginLogout relayerLoginLogout = new RelayerLoginLogout();
        relayerLoginLogout.setUid(uid);
        try {
            relayerCodec.encode(null, channelBuffer, relayerLoginLogout);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteBuffer byteBuffer = channelBuffer.toByteBuffer();


        Object obj = relayerCodec.decode(null, channelBuffer);

        logger.info("decode:"+obj);

        logger.info("send commend:"+byteBuffer+" uid:"+uid);
        nioConnection.sendCommandByteBuffer(byteBuffer);

        System.in.read();
    }
}
