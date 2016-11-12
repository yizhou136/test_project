package com.zy.nut.relayer.common.container;


import com.zy.nut.relayer.common.amqp.ServerendAMQPClient;
import com.zy.nut.relayer.common.configure.Configuration;
import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.remoting.RemotingException;
import com.zy.nut.relayer.common.remoting.exchange.TransformData;
import com.zy.nut.relayer.common.transporter.netty.NettyServer;


import java.io.IOException;
import java.net.URL;

/**
 * Created by Administrator on 2016/11/7.
 */
public class RelayerServerContainer extends AbstractContainer implements ContainerExchange{
    private static final Logger logger = LoggerFactory.getLogger(RelayerServerContainer.class);
    private NettyServer server;
    private ServerendAMQPClient serverendAMQPClient;
    private ConnectToLeadingClientContainer connectToLeadingClientContainer;

    public RelayerServerContainer(URL propertiesURL) throws Throwable{
        super(propertiesURL);
    }

    @Override
    public void init() throws Throwable{
        Configuration configuration = getConfiguration();
        if (configuration == null)
            return;

        try {
            server = new NettyServer(configuration, this);
        } catch (RemotingException e) {
            e.printStackTrace();
            throw e;
        }



        if (configuration.isClusterLeader()){//for leading server
            serverendAMQPClient = new ServerendAMQPClient(configuration, this);
        }else {
            connectToLeadingClientContainer = new ConnectToLeadingClientContainer(server,configuration);
            serverendAMQPClient = new ServerendAMQPClient(configuration, null);
        }
        //int min = configuration.getServerCluster().getMinTransmitterCount();
    }


    public void receiveFromServer(byte[] data) {

    }

    public void receiveFromBackend(byte[] data) {
        logger.info("receiveFromBackend data:"+data);
        getDecodedChannelBuffer().clear();
        getDecodedChannelBuffer().writeBytes(data);
        try {
            TransformData transformData = (TransformData)getCodec()
                    .decode(null,getDecodedChannelBuffer());
            sendToFrontEnd(transformData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendToFrontEnd(TransformData transformData) {
        server.sendToFrontEnd(transformData);
    }

    public void sendToLeadingServers(Object msg, boolean isFanout){
        if (connectToLeadingClientContainer == null) return;

        try {
            connectToLeadingClientContainer.sendDataToLeadingServers(msg,isFanout);
        }catch (RemotingException e){
            e.printStackTrace();
        }
    }

    public static void main(String argv[]) throws Throwable{
        URL url = RelayerServerContainer.class.getClassLoader().getResource("relayer.properties");
        RelayerServerContainer relayerContainer = new RelayerServerContainer(url);

        System.out.println(relayerContainer.getConfiguration());
    }
}
