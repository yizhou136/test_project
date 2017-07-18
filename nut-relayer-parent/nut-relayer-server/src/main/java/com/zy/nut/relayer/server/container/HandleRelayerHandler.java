package com.zy.nut.relayer.server.container;

import com.zy.nut.common.beans.exchange.*;
import com.zy.nut.common.msp.*;

import com.zy.nut.common.beans.DialogMsg;
import com.zy.nut.common.beans.RoomMsg;
import com.zy.nut.relayer.common.remoting.Codec;
import com.zy.nut.relayer.common.remoting.Server;
import com.zy.nut.relayer.server.service.SpringNettyContainer;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zhougb on 2016/11/9.
 */
@Component
@ChannelHandler.Sharable
public class HandleRelayerHandler extends ChannelDuplexHandler {
    private static final Logger logger = LoggerFactory.getLogger(HandleRelayerHandler.class);

    @Autowired
    private MsBackService msBackService;

    @Autowired
    private SpringNettyContainer springNettyContainer;

    private Server getServer(){
        return springNettyContainer.getServer();
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
        logger.info("HandleRelayerHandler  received msg");
        if (msg instanceof RelayerLogin){
            RelayerLogin userLogin = (RelayerLogin)msg;
            userLogin.setChannel(ctx.channel());
            logger.info("Uid:{} has logined", userLogin.getUid());
            getServer().userLogin(userLogin);
            msBackService.userLogin(userLogin.getUid(), getServer().getNodeName());
        }else if (msg instanceof RelayerLogout){
            RelayerLogout relayerLogout = (RelayerLogout)msg;
            logger.info("Uid:{} has logouted", relayerLogout.getUid());
            getServer().userLogout(relayerLogout);
            msBackService.userLogout(relayerLogout.getUid(), getServer().getNodeName());
        }else if (msg instanceof RelayerEnterRoom){
            RelayerEnterRoom relayerEnterRoom = (RelayerEnterRoom)msg;
            logger.info("Uid:{} has relayerEnterRoom", relayerEnterRoom.getUid());
            getServer().enterRoom(relayerEnterRoom);
            msBackService.notify(relayerEnterRoom);
        }else if (msg instanceof RelayerLeftRoom){
            RelayerLeftRoom relayerLeftRoom = (RelayerLeftRoom)msg;
            logger.info("Uid:{} has relayerLeftRoom", relayerLeftRoom.getUid());
            getServer().leftRoom(relayerLeftRoom);
            msBackService.notify(relayerLeftRoom);
        }else if (msg instanceof TransformData){
            TransformData transformData = (TransformData)msg;
            logger.info("transformData matchConditions::"+transformData.getMatchConditiones());
            //msgService.s
        }else if (msg instanceof DialogMsg){
            DialogMsg dialogMsg = (DialogMsg)msg;
            dialogMsg.setProxySendMs(System.currentTimeMillis());
            logger.info("HandleRelayerHandler  received dialogMsg:{}",
                    dialogMsg);

            msBackService.notify(dialogMsg);
        }else if (msg instanceof RoomMsg){
            RoomMsg roomMsg = (RoomMsg)msg;
            logger.info("roommsg "+roomMsg.getFuid()+" to "+roomMsg.getRid()
                    +" msg:"+roomMsg.getMsg());
            msBackService.notify(roomMsg);
        }
    }
}