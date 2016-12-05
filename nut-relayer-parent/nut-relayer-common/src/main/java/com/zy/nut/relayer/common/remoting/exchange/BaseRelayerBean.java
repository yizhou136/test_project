package com.zy.nut.relayer.common.remoting.exchange;

import io.netty.channel.Channel;

/**
 * Created by zhougb on 2016/12/5.
 */
public class BaseRelayerBean {
    private long uid;
    private Channel channel;


    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
