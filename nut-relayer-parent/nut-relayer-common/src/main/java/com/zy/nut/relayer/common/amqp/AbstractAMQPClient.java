package com.zy.nut.relayer.common.amqp;

import com.rabbitmq.client.*;
import com.zy.nut.relayer.common.configure.AMQPConf;
import com.zy.nut.relayer.common.configure.Cluster;
import com.zy.nut.relayer.common.configure.Configuration;
import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.remoting.exchange.TransfredData;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhougb on 2016/11/10.
 */
public abstract class AbstractAMQPClient {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractAMQPClient.class);
    private Configuration configuration;
    private Channel channel;
    protected ExecutorService executorService = Executors.newCachedThreadPool();

    protected String fanoutExchangeName;
    protected String topicExchangeName;

    public AbstractAMQPClient(Configuration configuration){
        this.configuration = configuration;
        fanoutExchangeName = generateFanoutExchangeName();
        topicExchangeName = generateTopicExchangeName();
    }

    public abstract void declares(String project)throws IOException;

    public void initChannel(){
        AMQPConf amqpConf = configuration.getAmqpConf();
        if (amqpConf == null);
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(amqpConf.getHost());
            connectionFactory.setPort(amqpConf.getPort());
            connectionFactory.setUsername(amqpConf.getUsername());
            connectionFactory.setPassword(amqpConf.getPassword());
            Connection connection = connectionFactory.newConnection();
            channel = connection.createChannel();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Channel generateChannel(){
        if (channel == null)
            initChannel();
        return channel;
    }

    public String getClusterName(){
        Cluster cluster = configuration.getServerCluster();
        if (cluster == null) return "";
        return cluster.getName();
    }



    public String generateSendQueueName(String project){
        return String.format("send_qu.%s.%s", getClusterName(), project);
    }

    /*public String generateExchangeName(){
        return String.format("%s_ex", getClusterName());
    }*/

    public String generateFanoutExchangeName(){
        return String.format("%s_ex_fanout", getClusterName());
    }
    public String generateTopicExchangeName(){
        return String.format("%s_ex_topic", getClusterName());
    }
    /*public String generateFanoutRoutingKey(){
        return String.format("", getClusterName());
    }*/
    public String generateTopicRoutingKey(String project, Set<String> clusterNames){
        StringBuilder stringBuilder = new StringBuilder(project);
        for (String clusterName:clusterNames)
            stringBuilder.append(".").append(clusterName);
        return stringBuilder.toString();
    }

    public String generateTopicBindlingKey(String project){
        return String.format("%s.#.%s.#", project,getClusterName());
    }


    /*public void transformDataToFrontend(TransfredData transfredData, boolean isFanout){
        String project = transfredData.getProject();
        String exchangeName = isFanout ? generateFanoutExchangeName() : generateTopicExchangeName();
        boolean durable = false;
        boolean autoDelete = true;
        Map<String, Object> arguments = null;
        Channel channel = generateChannel();

        try {
            String recvQueueName = generateRecvQueueName(project);
            AMQP.Queue.DeclareOk recvDeclareOk = channel.queueDeclare(recvQueueName, false, false, false, null);

            String sendQueueName = generateSendQueueName(project);
            AMQP.Queue.DeclareOk recvDeclareOk = channel.queueDeclare(recvQueueName, false, false, false, null);
            AMQP.Exchange.DeclareOk declareOk = channel.exchangeDeclare(exchangeName,
                    type, durable, autoDelete, arguments);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/




    public void stopAMQPReceiver(){
        if (executorService != null){
            executorService.shutdown();
        }
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }
}
