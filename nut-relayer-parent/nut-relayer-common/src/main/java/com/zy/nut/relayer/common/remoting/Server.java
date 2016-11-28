package com.zy.nut.relayer.common.remoting;

import com.zy.nut.relayer.common.remoting.exchange.RelayerRegisteringUnRegistering;
import com.zy.nut.relayer.common.remoting.exchange.TransformData;

import java.net.InetSocketAddress;
import java.util.Collection;

/**
 * Created by Administrator on 2016/11/6.
 */
public interface Server {

    void handleRegUnreg(Channel channel,RelayerRegisteringUnRegistering relayerRegisteringUnRegistering);

    void sendToFrontEnd(TransformData transformData);

    void sendToBackEnd(TransformData transformData);

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