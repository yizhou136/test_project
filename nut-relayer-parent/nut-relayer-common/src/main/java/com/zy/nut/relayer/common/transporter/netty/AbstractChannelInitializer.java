package com.zy.nut.relayer.common.transporter.netty;

import com.zy.nut.relayer.common.URL;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by zhougb on 2016/11/9.
 */
public abstract class AbstractChannelInitializer extends ChannelInitializer<SocketChannel> {
    protected URL url;

    public AbstractChannelInitializer(URL url){
        this.url = url;
    }
}
