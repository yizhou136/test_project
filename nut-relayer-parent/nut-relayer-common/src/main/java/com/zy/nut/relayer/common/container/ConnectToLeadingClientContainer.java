package com.zy.nut.relayer.common.container;

import com.zy.nut.relayer.common.configure.Configuration;
import com.zy.nut.relayer.common.remoting.RemotingException;
import com.zy.nut.relayer.common.remoting.Server;
import com.zy.nut.relayer.common.transporter.netty.NettyClient;

import java.util.Set;

/**
 * Created by zhougb on 2016/11/11.
 */
public class ConnectToLeadingClientContainer extends AbstractRelayerClientContainer{

    public ConnectToLeadingClientContainer(Server server,Configuration configuration)throws Throwable{
        super(server,configuration);
    }

    @Override
    public void init(Configuration configuration) throws RemotingException{
        Set<String> leadingServerAddress = configuration.getLeadingServerAddress();
        for (String leadingSrv : leadingServerAddress){
            Configuration serverclientConf = new Configuration(configuration);
            serverclientConf.setScConnectedAddress(leadingSrv);
            NettyClient nettyClient = new NettyClient(serverclientConf, bootstrap, nioEventLoopGroup);
            //nettyClient.setConnectFutureListener(new ServerClientConnectionFutureListener());
            serverClients.add(nettyClient);
        }
    }
}
