package com.zy.nut.relayer.common.transporter.netty;

import com.zy.nut.relayer.common.configure.Configuration;
import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.remoting.RemotingException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * Created by Administrator on 2016/11/6.
 */
public class ServerClientNettyClient extends NettyClient {
    private static final Logger logger = LoggerFactory.getLogger(ServerClientNettyClient.class);

    public ServerClientNettyClient(Configuration configuration,
                                   Bootstrap bootstrap,
                                   NioEventLoopGroup nioEventLoopGroup) throws RemotingException {
        super(configuration);
        setBootstrap(bootstrap);
        setNioEventLoopGroup(nioEventLoopGroup);
    }
}
