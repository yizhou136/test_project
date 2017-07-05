package com.zy.nut.relayer.common.transporter.netty;

import com.zy.nut.common.beans.exchange.*;
import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.remoting.Server;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by zhougb on 2016/11/9.
 */
public class HandleFrontClientHandler extends ChannelDuplexHandler {
    private static final Logger logger = LoggerFactory.getLogger(HandleFrontClientHandler.class);
    private Server server;
    public HandleFrontClientHandler(Server server){
        this.server = server;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelInactive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RelayerLogin){
            RelayerLogin relayerLogin = (RelayerLogin)msg;
            logger.info("Uid:"+relayerLogin.getUid()+" has logined");
        }else if (msg instanceof RelayerLogout){
            RelayerLogout relayerLogout = (RelayerLogout)msg;
            logger.info("Uid:"+relayerLogout.getUid()+" has logouted");
        }else if (msg instanceof RelayerRegisteringUnRegistering){
            RelayerRegisteringUnRegistering relayerRegistering = (RelayerRegisteringUnRegistering)msg;
            logger.info("receive RelayerRegisteringUnRegistering clientaddr:"+ctx.channel().remoteAddress()
                +" relayerRegistering:"+relayerRegistering);
            server.handleRegUnreg(new NettyChannel(ctx.channel(), null),
                    relayerRegistering);
        }else if (msg instanceof RelayerPingPong){
        }else if (msg instanceof RelayerElecting){
        }else if (msg instanceof TransformData){
            server.sendToBackEnd((TransformData) msg);
        }
    }
}
