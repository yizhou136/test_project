package com.zy.nut.relayer.common.container;


import com.zy.nut.relayer.common.configure.Configuration;
import com.zy.nut.relayer.common.remoting.RemotingException;
import com.zy.nut.relayer.common.transporter.netty.NettyClient;
import com.zy.nut.relayer.common.transporter.netty.NettyServer;


import java.net.URL;
import java.util.Set;

/**
 * Created by Administrator on 2016/11/7.
 */
public class RelayerServerContainer extends AbstractContainer implements Container{
    private NettyServer server;
    private RelayerClientContainer relayerClientContainer;

    public RelayerServerContainer(URL propertiesURL) throws Throwable{
        super(propertiesURL);
    }

    @Override
    public void init() throws Throwable{
        Configuration configuration = getConfiguration();
        if (configuration == null)
            return;

        try {
            server = new NettyServer(configuration);
        } catch (RemotingException e) {
            e.printStackTrace();
            throw e;
        }

        if (configuration.isClusterLeader()){

        }else {

        }
        //int min = configuration.getServerCluster().getMinTransmitterCount();
    }


    public static void main(String argv[]) throws Throwable{
        URL url = RelayerServerContainer.class.getClassLoader().getResource("relayer.properties");
        RelayerServerContainer relayerContainer = new RelayerServerContainer(url);

        System.out.println(relayerContainer.getConfiguration());
    }
}
