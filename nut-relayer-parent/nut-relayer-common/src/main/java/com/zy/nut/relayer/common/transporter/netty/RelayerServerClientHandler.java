package com.zy.nut.relayer.common.transporter.netty;

import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.remoting.Server;
import com.zy.nut.common.beans.exchange.TransformData;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;

/**
 * Created by zhougb on 2016/11/11.
 */
public class RelayerServerClientHandler extends ChannelDuplexHandler{
    private static final Logger logger = LoggerFactory.getLogger(RelayerServerClientHandler.class);
    private Server server;

    public RelayerServerClientHandler(Server server){
        this.server = server;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //super.channelActive(ctx);
        logger.info("connected to server:"+ctx.channel().remoteAddress()+" and do register.");
        /*RelayerRegisteringUnRegistering relayerRegisteringUnRegistering
                = new RelayerRegisteringUnRegistering();
        relayerRegisteringUnRegistering.setRegisterType(
                RelayerRegisteringUnRegistering.RelayerRegisteringType.SERVER_REG_CLIENT.getType());
        ctx.writeAndFlush(relayerRegisteringUnRegistering);*/
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("receive data from "+ctx.channel().remoteAddress()+" msg:"+msg);
        if (msg instanceof TransformData){
            server.sendToFrontEnd((TransformData) msg);
        }
        super.channelRead(ctx, msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}