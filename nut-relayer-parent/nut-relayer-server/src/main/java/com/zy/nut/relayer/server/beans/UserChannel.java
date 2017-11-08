package com.zy.nut.relayer.server.beans;

import com.zy.nut.relayer.common.remoting.RemotingException;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2016/2/4.
 */
public class UserChannel {
    private static final Map<Long, Set<UserChannel>> GlobalUserChannels = new ConcurrentHashMap();

    private Long uid;
    private Channel channel;


    public UserChannel(Channel nettyChannel, Long uid){
        this.channel = nettyChannel;
        this.uid = uid;
    }

    public static final UserChannel getOrAddChannel(Channel channel, long uid){
        Set<UserChannel> userChannelSet = GlobalUserChannels.get(uid);
        if (userChannelSet == null){
            userChannelSet = new HashSet<>();
            GlobalUserChannels.put(uid, userChannelSet);
        }

        for (UserChannel userChannel : userChannelSet){
            if (userChannel.getUid() == uid && userChannel.getChannel().equals(channel))
                return userChannel;
        }

        UserChannel userChannel = new UserChannel(channel,uid);
        userChannelSet.add(userChannel);
        return userChannel;
    }

    public static final Set<UserChannel> getByUid(long uid){
        return GlobalUserChannels.get(uid);
    }

    public static final boolean removeUserChannel(Channel channel, long uid){
        Set<UserChannel> userChannelSet = GlobalUserChannels.get(uid);
        if (userChannelSet != null){
            UserChannel userChannel = new UserChannel(channel,uid);
            return userChannelSet.remove(userChannel);
        }
        return false;
    }

    public void send(Object message) throws RemotingException{
        send(message, false);
    }

    public void send(Object message, boolean sent) throws RemotingException{
        channel.writeAndFlush(message);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserChannel){
            UserChannel oth = (UserChannel) obj;
            if (getUid() == oth.getUid() &&  getChannel().equals(oth.getChannel())){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int key = getUid().intValue();
        key += ~(key << 15);
        key ^= (key >>> 10);
        key += (key << 3);
        key ^= (key >>> 6);
        key += ~(key << 11);
        key ^= (key >>> 16);
        return key;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
    /*@Override
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
    }*/
}
