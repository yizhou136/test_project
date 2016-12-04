package com.zy.nut.relayer.client.Container;

import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.remoting.buffer.ChannelBuffer;
import com.zy.nut.relayer.common.remoting.buffer.ChannelBuffers;
import com.zy.nut.relayer.common.remoting.exchange.RelayerLogin;
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
    private static NioConnection nioConnection;

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

    public static void login(){
        Random random = new Random(10000);
        long uid = Math.abs(random.nextLong());
        RelayerLogin relayerLogin = new RelayerLogin();
        relayerLogin.setUid(uid);
        relayerLogin.setPid((byte)0);
        relayerLogin.setUserName("zgb");
        relayerLogin.setPassword("123456");
        ByteBuffer byteBuffer = encode(relayerLogin);
        logger.info("login encode byteBuffer:"+byteBuffer);
        relayerLogin = (RelayerLogin) decode(channelBuffer);
        logger.info("login decode obj:"+relayerLogin);
        nioConnection.sendCommandByteBuffer(byteBuffer);
        logger.info("send commend:"+byteBuffer+" uid:"+uid);
        channelBuffer.clear();
        /*String uidstr = "1234";
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
        logger.info("send relayerRegisteringUnRegistering commend:"+byteBuffer+" uid:"+uid);*/
    }

    public static void doTransform(){
        String project = "cuctv.weibo";
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
        ByteBuffer byteBuffer = channelBuffer.toByteBuffer();
        nioConnection.sendCommandByteBuffer(byteBuffer);
    }

    public static void init(){
        NioConnection.HostPortPair hostPortPair = new NioConnection.HostPortPair("127.0.0.1", 8383);
        List<NioConnection.HostPortPair> list = new ArrayList<NioConnection.HostPortPair>();
        list.add(hostPortPair);
        nioConnection = new NioConnection(list);
        nioConnection.doConnection();
    }

    public static void main(String argv[]) throws Exception{
        init();
        login();
        Thread.sleep(5000);
    }
}
