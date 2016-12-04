package com.zy.nut.relayer.server.beans;

import com.zy.nut.relayer.common.URL;
import com.zy.nut.relayer.common.remoting.Channel;
import com.zy.nut.relayer.common.remoting.RemotingException;
import com.zy.nut.relayer.common.transporter.netty.NettyChannel;

import java.net.InetSocketAddress;

/**
 * Created by Administrator on 2016/12/4.
 */
public class UserChannel implements Channel{
    private User user;
    private NettyChannel channel;


    public UserChannel(NettyChannel nettyChannel, User user){
        this.channel = nettyChannel;
        this.user = user;
    }

    @Override
    public String getChannelId() {
        return channel.getChannelId();
    }

    @Override
    public void send(Object message, boolean sent) throws RemotingException {
        channel.send(message, sent);
    }

    @Override
    public URL getUrl() {
        return channel.getUrl();
    }

    @Override
    public InetSocketAddress getRemoteAddress() {
        return channel.getRemoteAddress();
    }

    @Override
    public InetSocketAddress getLocalAddress() {
        return channel.getLocalAddress();
    }

    @Override
    public boolean isConnected() {
        return channel.isConnected();
    }

    @Override
    public boolean hasAttribute(String key) {
        return channel.hasAttribute(key);
    }

    @Override
    public Object getAttribute(String key) {
        return channel.getAttribute(key);
    }

    @Override
    public void setAttribute(String key, Object value) {
        channel.setAttribute(key, value);
    }

    @Override
    public void removeAttribute(String key) {
        channel.removeAttribute(key);
    }
}
