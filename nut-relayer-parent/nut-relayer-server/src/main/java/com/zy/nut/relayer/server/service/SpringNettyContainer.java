package com.zy.nut.relayer.server.service;

import com.zy.nut.relayer.common.remoting.exchange.*;
import com.zy.nut.relayer.common.transporter.ChannelInitializerRegister;

/**
 * Created by Administrator on 2016/12/4.
 */
public interface SpringNettyContainer {

    void login(RelayerLogin relayerLogin);

    void logout(RelayerLogout relayerLogout);

    void enterRoom(RelayerEnterRoom relayerEnterRoom);

    void leftRoom(RelayerLeftRoom relayerLeftRoom);

    void sendTransformData(TransformData transformData);

    void addChannelInitializerRegister(ChannelInitializerRegister channelInitializerRegister);
}
