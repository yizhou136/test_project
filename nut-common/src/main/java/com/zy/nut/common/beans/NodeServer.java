package com.zy.nut.common.beans;

import io.netty.channel.Channel;

import java.util.Set;

/**
 * @author by zy.
 */
public class NodeServer {
    private Channel channel;
    private Set<Long> roomIdSet;


    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Set<Long> getRoomIdSet() {
        return roomIdSet;
    }

    public void setRoomIdSet(Set<Long> roomIdSet) {
        this.roomIdSet = roomIdSet;
    }
}
