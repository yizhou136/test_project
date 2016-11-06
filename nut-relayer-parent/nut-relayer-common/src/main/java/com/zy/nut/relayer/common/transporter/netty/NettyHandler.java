/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zy.nut.relayer.common.transporter.netty;


import com.zy.nut.relayer.common.URL;
import com.zy.nut.relayer.common.remoting.Channel;
import com.zy.nut.relayer.common.remoting.ChannelHandler;
import com.zy.nut.relayer.common.utils.NetUtils;
import io.netty.channel.ChannelDuplexHandler;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;


import java.net.InetSocketAddress;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@io.netty.channel.ChannelHandler.Sharable
public class NettyHandler extends ChannelDuplexHandler {
    private final Map<String, Channel> channels = new ConcurrentHashMap();
    private final URL url;
    private final ChannelHandler handler;

    public NettyHandler(URL url, ChannelHandler handler) {
        if(url == null) {
            throw new IllegalArgumentException("url == null");
        } else if(handler == null) {
            throw new IllegalArgumentException("handler == null");
        } else {
            this.url = url;
            this.handler = handler;
        }
    }

    public Map<String, Channel> getChannels() {
        return this.channels;
    }

    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), this.url, this.handler);

        try {
            if(channel != null) {
                this.channels.put(NetUtils.toAddressString((InetSocketAddress)ctx.channel().remoteAddress()), channel);
            }

            this.handler.connected(channel);
        } finally {
            NettyChannel.removeChannelIfDisconnected(ctx.channel());
        }

    }

    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), this.url, this.handler);

        try {
            this.channels.remove(NetUtils.toAddressString((InetSocketAddress)ctx.channel().remoteAddress()));
            this.handler.disconnected(channel);
        } finally {
            NettyChannel.removeChannelIfDisconnected(ctx.channel());
        }

    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), this.url, this.handler);

        try {
            this.handler.received(channel, msg);
        } finally {
            NettyChannel.removeChannelIfDisconnected(ctx.channel());
        }

    }

    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        super.write(ctx, msg, promise);
        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), this.url, this.handler);

        try {
            this.handler.sent(channel, msg);
        } finally {
            NettyChannel.removeChannelIfDisconnected(ctx.channel());
        }

    }

    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        NettyChannel channel = NettyChannel.getOrAddChannel(ctx.channel(), this.url, this.handler);

        try {
            this.handler.caught(channel, cause);
        } finally {
            NettyChannel.removeChannelIfDisconnected(ctx.channel());
        }

    }
}