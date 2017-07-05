package com.zy.nut.relayer.common.remoting;

import com.zy.nut.common.beans.DialogMsg;
import com.zy.nut.common.beans.RoomMsg;
import com.zy.nut.common.beans.exchange.*;

/**
 * Created by Administrator on 2016/11/6.
 */
public interface Server {

    void handleRegUnreg(Channel channel,RelayerRegisteringUnRegistering relayerRegisteringUnRegistering);

    void sendToFrontEnd(TransformData transformData);

    void sendToBackEnd(TransformData transformData);

    void heartbeat();

    String getNodeName();

    void userLogin(RelayerLogin relayerLogin);

    void userLogout(RelayerLogout relayerLogout);

    void enterRoom(RelayerEnterRoom relayerEnterRoom);

    void leftRoom(RelayerLeftRoom relayerLeftRoom);

    void sendTo(DialogMsg dialogMsg);

    void publish(RoomMsg roomMsg);

    //boolean isBound();
    /**
     * get channels.
     *
     * @return channels
     */
    //Collection<Channel> getChannels();

    /**
     * get channel.
     *
     * @param remoteAddress
     * @return channel
     */
    //Channel getChannel(InetSocketAddress remoteAddress);
}