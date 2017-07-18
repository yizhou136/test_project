package com.zy.nut.relayer.client.netty;

import com.zy.nut.common.beans.DialogMsg;
import com.zy.nut.common.beans.RoomMsg;
import com.zy.nut.common.beans.exchange.RelayerLogin;
import com.zy.nut.common.beans.exchange.RelayerLogout;
import com.zy.nut.common.beans.exchange.TransformData;
import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.remoting.Codec;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;

/**
 * Created by zhougb on 2016/11/9.
 */
@ChannelHandler.Sharable
public class ClientHandleRelayerHandler extends ChannelDuplexHandler {
    private static final Logger logger = LoggerFactory.getLogger(ClientHandleRelayerHandler.class);

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
        long startDecodeMs = Codec.StartDecodeMS.get();
        long endDecodeMs = System.currentTimeMillis();
        Codec.StartDecodeMS.set(0L);

        if (msg instanceof RelayerLogin){
            RelayerLogin relayerLogin = (RelayerLogin)msg;
            relayerLogin.setChannel(ctx.channel());
            logger.info("Uid:"+relayerLogin.getUid()+" has logined");
        }else if (msg instanceof RelayerLogout){
            RelayerLogout relayerLogout = (RelayerLogout)msg;
            logger.info("Uid:"+relayerLogout.getUid()+" has logouted");
        }else if (msg instanceof TransformData){
            TransformData transformData = (TransformData)msg;
            logger.info("transformData matchConditions::"+transformData.getMatchConditiones());
            //msgService.s
        }else if (msg instanceof DialogMsg){
            DialogMsg dialogMsg = (DialogMsg)msg;
            dialogMsg.setCtime(Codec.ReceiveDataStartMS.get());
            long clientDecodeMs = endDecodeMs - startDecodeMs;
            logger.info("HandleRelayerHandler  received dialogMsg:{}"+
                    dialogMsg+" clientDecodeMs:"+clientDecodeMs);

            long escapeMs = System.currentTimeMillis() - dialogMsg.getLctime();
            logger.warn("receive DialogMsg "+dialogMsg.getFuid()+" to "+dialogMsg.getTuid()
                    +" msg:"+dialogMsg.getMsg()+"  escapeMs:"+escapeMs+"ms");
        }else if (msg instanceof RoomMsg){
            RoomMsg roomMsg = (RoomMsg)msg;
            logger.info("roommsg "+roomMsg.getFuid()+" to "+roomMsg.getRid()
                    +" msg:"+roomMsg.getMsg());
        }
    }
}