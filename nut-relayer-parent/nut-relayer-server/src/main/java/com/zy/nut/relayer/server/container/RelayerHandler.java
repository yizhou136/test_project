package com.zy.nut.relayer.server.container;

import com.zy.nut.relayer.common.remoting.Channel;
import com.zy.nut.relayer.common.remoting.ChannelHandler;
import com.zy.nut.relayer.common.remoting.RemotingException;

/**
 * Created by zhougb on 2016/11/8.
 */
public class RelayerHandler implements ChannelHandler{
    public void connected(Channel channel) throws RemotingException {

    }

    public void disconnected(Channel channel) throws RemotingException {

    }

    public void sent(Channel channel, Object message) throws RemotingException {

    }

    public void received(Channel channel, Object message) throws RemotingException {

    }

    public void caught(Channel channel, Throwable exception) throws RemotingException {

    }
}
