package com.zy.nut.relayer.server.container;

import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.remoting.Server;
import com.zy.nut.relayer.common.remoting.exchange.*;
import com.zy.nut.relayer.common.transporter.netty.NettyChannel;
import com.zy.nut.relayer.server.service.UserService;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zhougb on 2016/11/9.
 */
@Component
public class HandleRelayerHandler extends ChannelDuplexHandler {
    private static final Logger logger = LoggerFactory.getLogger(HandleRelayerHandler.class);

    @Autowired
    private UserService userService;


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
            userService.login(relayerLogin);
        }else if (msg instanceof RelayerLogout){
            RelayerLogout relayerLogout = (RelayerLogout)msg;
            logger.info("Uid:"+relayerLogout.getUid()+" has logouted");
            userService.logout(relayerLogout);
        }else if (msg instanceof TransformData){

        }
    }
}