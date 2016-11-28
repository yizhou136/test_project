package com.zy.nut.relayer.client.Container;

import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.remoting.buffer.ChannelBuffer;
import com.zy.nut.relayer.common.remoting.buffer.ChannelBuffers;
import com.zy.nut.relayer.common.remoting.exchange.RelayerLoginLogout;
import com.zy.nut.relayer.common.remoting.exchange.RelayerRegisteringUnRegistering;
import com.zy.nut.relayer.common.remoting.exchange.TransformData;
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
    private static ChannelBuffer channelBuffer = ChannelBuffers.buffer(1024);
    private static RelayerCodec relayerCodec = new RelayerCodec();

    public static ByteBuffer encode(Object obj){
        try {
            relayerCodec.encode(null, channelBuffer, obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return channelBuffer.toByteBuffer();
    }

    public static Object decode(ChannelBuffer channelBuffer){
        try {
            return relayerCodec.decode(null, channelBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String argv[]) throws Exception{
        String project = "cuctv.weibo";
        Random random = new Random(10000);
        NioConnection.HostPortPair hostPortPair = new NioConnection.HostPortPair("127.0.0.1", 8484);
        List<NioConnection.HostPortPair> list = new ArrayList<NioConnection.HostPortPair>();
        list.add(hostPortPair);
        NioConnection nioConnection = new NioConnection(list);
        nioConnection.doConnection();


        long uid = Math.abs(random.nextLong());
        RelayerLoginLogout relayerLoginLogout = new RelayerLoginLogout();
        relayerLoginLogout.setUid(uid);
        ByteBuffer byteBuffer = encode(relayerLoginLogout);
        logger.info("encode byteBuffer:"+byteBuffer);
        relayerLoginLogout = (RelayerLoginLogout) decode(channelBuffer);
        logger.info("decode obj:"+relayerLoginLogout);

        logger.info("send commend:"+byteBuffer+" uid:"+uid);
        nioConnection.sendCommandByteBuffer(byteBuffer);


        channelBuffer.clear();
        String uidstr = "1234";
        RelayerRegisteringUnRegistering relayerRegisteringUnRegistering = new RelayerRegisteringUnRegistering();
        relayerRegisteringUnRegistering.setRegisterType(RelayerRegisteringUnRegistering
                .RelayerRegisteringType.NORMAL_REG_CLIENT.getType());
        relayerRegisteringUnRegistering.setProject(project);
        relayerRegisteringUnRegistering.setMatchType(TransformData.TRANSFORM_DATA_TYPE.DIRECT.getType());
        relayerRegisteringUnRegistering.setMatchConditiones(uidstr);

        channelBuffer.clear();
        encode(relayerRegisteringUnRegistering);
        byteBuffer = channelBuffer.toByteBuffer();
        nioConnection.sendCommandByteBuffer(byteBuffer);
        logger.info("send relayerRegisteringUnRegistering commend:"+byteBuffer+" uid:"+uid);

        TransformData transformData = new TransformData();
        transformData.setProject(project);
        transformData.setExchangeType(TransformData.TRANSFORM_DATA_TYPE.FANOUT.getType());
        transformData.setData("afasdfasdf");
        //transformData.setMatchType();
        /*if (clusterNames == null || clusterNames.isEmpty()) {
            transformData.setExchangeType(TransformData.TRANSFORM_DATA_TYPE.FANOUT.getType());
            //transformData.setRoutingKey();
        }else if (clusterNames.size() == 1){
            String routingkey = clusterNames.iterator().next();
            transformData.setExchangeType(TransformData.TRANSFORM_DATA_TYPE.DIRECT.getType());
            transformData.setRoutingKey(routingkey);
        }else {
            String routingkey = genTopicRoutingKey(getDefaultProject(), clusterNames);
            transformData.setExchangeType(TransformData.TRANSFORM_DATA_TYPE.TOPIC.getType());
            transformData.setRoutingKey(routingkey);
        }
        transformData.setMatchConditiones(matchConditions);
        transformData.setData(msg);*/

        channelBuffer.clear();
        encode(transformData);
        byteBuffer = channelBuffer.toByteBuffer();
        nioConnection.sendCommandByteBuffer(byteBuffer);

        System.in.read();
    }
}
