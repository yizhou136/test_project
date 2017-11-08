/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.zy.nut.relayer.server.handlers.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zy.nut.common.beans.BaseCommand;
import com.zy.nut.common.beans.BaseMsg;
import com.zy.nut.common.beans.DialogMsg;
import com.zy.nut.common.beans.exchange.BaseRelayerBean;
import com.zy.nut.common.beans.exchange.RelayerLogin;
import com.zy.nut.common.msp.MsBackService;
import com.zy.nut.relayer.common.remoting.Server;
import com.zy.nut.relayer.common.transporter.netty.NettyServer;
import com.zy.nut.relayer.server.service.SpringNettyContainer;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;

/**
 * Echoes uppercase content of text frames.
 */
@Component
@ChannelHandler.Sharable
public class WebSocketFrameHandler extends SimpleChannelInboundHandler<WebSocketFrame> {
    private static final Logger logger = LoggerFactory.getLogger(WebSocketFrameHandler.class);

    @Autowired
    private MsBackService msBackService;
    @Autowired
    private SpringNettyContainer springNettyContainer;

    private Server getServer(){
        return springNettyContainer.getServer();
    }

    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame frame) throws Exception {
        // ping and pong frames already handled

        if (frame instanceof TextWebSocketFrame) {
            // Send the uppercase string back.
            String text = ((TextWebSocketFrame) frame).text();
            logger.info("{} received txt:{}",ctx.channel(), text);
            BaseCommand baseCommand = convertToBaseCommand(text);
            logger.info("{} received txt:{} baseCommand:{}",
                        ctx.channel(), text, baseCommand);

            if (baseCommand.getCmdType() == 1){
                RelayerLogin userLogin = convertToUserLogin(baseCommand.getData());
                Channel channel = ctx.channel();
                Attribute<Boolean> isWebSocketChannel = channel.attr(NettyServer.IS_WEBSOCKET_CHANNEL);
                isWebSocketChannel.set(Boolean.TRUE);
                userLogin.setChannel(channel);
                logger.info("Uid:{} has logined", userLogin.getUid());
                getServer().userLogin(userLogin);
                msBackService.userLogin(userLogin.getUid(), getServer().getNodeName());

                ctx.channel().writeAndFlush(new TextWebSocketFrame("user "+userLogin.getUid()+" has login."));
            }else if (baseCommand.getCmdType() == 2){
                DialogMsg dialogMsg = convertToDialogMsg(baseCommand.getData());
                dialogMsg.setProxySendMs(System.currentTimeMillis());
                logger.info("HandleRelayerHandler  received dialogMsg:{}",
                        dialogMsg);
                msBackService.notify(dialogMsg);

                String msg = String.format("%d => %d: %s",
                        dialogMsg.getFuid(), dialogMsg.getTuid(), dialogMsg.getMsg());
                ctx.channel().writeAndFlush(new TextWebSocketFrame(msg));
            }
        } else {
            String message = "unsupported frame type: " + frame.getClass().getName();
            throw new UnsupportedOperationException(message);
        }
    }

    private BaseCommand convertToBaseCommand(String txt){
        try {
            return objectMapper.readValue(txt, BaseCommand.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private RelayerLogin convertToUserLogin(String txt){
        try {
            return objectMapper.readValue(txt, RelayerLogin.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private DialogMsg convertToDialogMsg(String txt){
        try {
            return objectMapper.readValue(txt, DialogMsg.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
