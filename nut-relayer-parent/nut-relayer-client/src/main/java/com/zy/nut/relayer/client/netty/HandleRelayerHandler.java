package com.zy.nut.relayer.client.netty;

import com.zy.nut.common.msp.MsBackService;
import com.zy.nut.common.msp.Response;
import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by zhougb on 2016/11/9.
 */
@ChannelHandler.Sharable
public class HandleRelayerHandler extends ChannelDuplexHandler {
    private static final Logger logger = LoggerFactory.getLogger(HandleRelayerHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("channelActive ctx:"+ctx);
        ctx.fireChannelActive();
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("channelInactive ctx:"+ctx);
        ctx.fireChannelInactive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("channelRead msg:"+msg);
        Response response = null;//msBackService.nofity("loginorlogout".getBytes());
        logger.info("channelRead response:"+response);
        /*if (msg instanceof RelayerLogin){
            RelayerLogin relayerLogin = (RelayerLogin)msg;
            relayerLogin.setChannel(ctx.channel());
            logger.info("Uid:"+relayerLogin.getUid()+" has logined");
            userService.login(relayerLogin);
        }else if (msg instanceof RelayerLogout){
            RelayerLogout relayerLogout = (RelayerLogout)msg;
            logger.info("Uid:"+relayerLogout.getUid()+" has logouted");
            userService.logout(relayerLogout);
        }else if (msg instanceof TransformData){
            TransformData transformData = (TransformData)msg;
            logger.info("transformData matchConditions::"+transformData.getMatchConditiones());
            //msgService.s
        }else if (msg instanceof DialogMsg){
            DialogMsg dialogMsg = (DialogMsg)msg;
            logger.info("DialogMsg "+dialogMsg.getFuid()+" to "+dialogMsg.getTuid()
                    +" msg:"+dialogMsg.getMsg());
            userService.sendDialogMsg(dialogMsg);
        }else if (msg instanceof RoomMsg){
            RoomMsg roomMsg = (RoomMsg)msg;
            logger.info("roommsg "+roomMsg.getFuid()+" to "+roomMsg.getRid()
                    +" msg:"+roomMsg.getMsg());
            userService.sendRoomMsg(roomMsg);
        }*/
    }
}