package com.zy.nut.relayer.common.amqp;

import com.rabbitmq.client.*;
import com.zy.nut.relayer.common.configure.AMQPConf;
import com.zy.nut.relayer.common.configure.Cluster;
import com.zy.nut.relayer.common.configure.Configuration;
import com.zy.nut.relayer.common.container.ContainerExchange;
import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhougb on 2016/11/10.
 */
public abstract class AbstractAMQPClient{
    protected static final Logger logger = LoggerFactory.getLogger(AbstractAMQPClient.class);
    private Configuration configuration;
    private Channel channel;
    protected ExecutorService executorService = Executors.newCachedThreadPool();
    private ContainerExchange containerExchange;

    public AbstractAMQPClient(Configuration configuration){
        this(configuration, null);
    }

    public AbstractAMQPClient(Configuration configuration, ContainerExchange containerExchange){
        this.configuration = configuration;
        this.containerExchange = containerExchange;
        initAMQPReceiver();
    }

    public abstract void initAMQPReceiver();

    public void declares(String project)throws IOException {
        boolean exculsive = false;
        boolean durable = false;
        boolean autoDelete = true;
        Channel channel = generateChannel();
        //declare for backend
        //for receive
        String backendRecvQueueName = generateBackendRecvQueueName(project);
        AMQP.Queue.DeclareOk recvDeclareOk = channel.queueDeclare(backendRecvQueueName, durable, exculsive, autoDelete, null);
        logger.info("backend recvDeclareOk:"+recvDeclareOk);

        //for send
        AMQP.Exchange.DeclareOk fanoutExchangeDeclareOk = channel.exchangeDeclare(
                generateBackendFanoutExchangeName(),
                "fanout", durable, autoDelete, null);
        AMQP.Exchange.DeclareOk topicExchangeDeclareOk = channel.exchangeDeclare(
                generateBackendTopicExchangeName(),
                "topic", durable, autoDelete, null);
        AMQP.Exchange.DeclareOk directExchangeDeclareOk = channel.exchangeDeclare(
                generateBackendDirectExchangeName(),
                "direct", durable, autoDelete, null);
        /*channel.queueBind(recvQueueName,fanoutExchangeName,null);
        channel.queueBind(recvQueueName,topicExchangeName,generateTopicBindlingKey(project));*/


        //declare for server
        //for receive queue
        String serverRecvQueueName = generateServerRecvQueueName(project);
        recvDeclareOk = channel.queueDeclare(serverRecvQueueName, durable, exculsive, autoDelete, null);
        logger.info("server recvDeclareOk:"+recvDeclareOk);

        //for send
        fanoutExchangeDeclareOk = channel.exchangeDeclare(
                generateServerFanoutExchangeName(),
                "fanout", durable, autoDelete, null);
        topicExchangeDeclareOk = channel.exchangeDeclare(
                generateServerTopicExchangeName(),
                "topic", durable, autoDelete, null);
        directExchangeDeclareOk = channel.exchangeDeclare(
                generateServerDirectExchangeName(),
                "direct", durable, autoDelete, null);

        //binding key for backend
        channel.queueBind(backendRecvQueueName,
                generateBackendTopicExchangeName(), generateBackendTopicBindlingKey(project));
        channel.queueBind(backendRecvQueueName,
                generateBackendFanoutExchangeName(), "");
        channel.queueBind(backendRecvQueueName,
                generateBackendDirectExchangeName(), generateBackendDirectRoutingBindlingKey(project));

        //binding key for server
        channel.queueBind(serverRecvQueueName,
                generateServerTopicExchangeName(), generateServerTopicBindlingKey(project));
        channel.queueBind(serverRecvQueueName,
                generateServerFanoutExchangeName(), "");
        channel.queueBind(serverRecvQueueName,
                generateServerDirectExchangeName(), generateServerDirectRoutingBindlingKey(project, getClusterName()));
    }

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


    public String generateBackendRecvQueueName(String project) {
        return String.format("backend.recvq.%s", project);
    }

    public String generateServerRecvQueueName(String project) {
        return String.format("server.recvq.%s.%s", project, getClusterName());
    }

    public String generateBackendSendQueueName(String project){
        return generateServerRecvQueueName(project);
    }

    public String generateServerSendQueueName(String project){
        return generateBackendRecvQueueName(project);
    }

    public String generateServerFanoutExchangeName() { return "server_ex_fanout";}
    public String generateServerTopicExchangeName(){
        return "server_ex_topic";
    }
    public String generateServerDirectExchangeName() { return "server_ex_direct";}

    public String generateBackendFanoutExchangeName(){
        return "backend_ex_fanout";
    }
    public String generateBackendTopicExchangeName() { return "backend_ex_topic";}
    public String generateBackendDirectExchangeName(){
        return "backend_ex_direct";
    }

    public String generateServerTopicRoutingKey(String project, Set<String> clusterNames){
        StringBuilder stringBuilder = new StringBuilder(project);
        for (String clusterName:clusterNames)
            stringBuilder.append(".").append(clusterName);
        return stringBuilder.toString();
    }

    public String generateServerTopicBindlingKey(String project){
        return String.format("%s.#.%s.#", project,getClusterName());
    }

    public String generateServerDirectRoutingBindlingKey(String project, String clusterName){
        return String.format("ferk_%s_%s", project, clusterName);
    }

    public String generateBackendTopicRoutingKey(Set<String> projects){
        StringBuilder stringBuilder = new StringBuilder("berk");
        for (String project:projects)
            stringBuilder.append(".").append(project);
        return stringBuilder.toString();
    }

    public String generateBackendTopicBindlingKey(String project){
        return String.format("berk.#.%s.#", project);
    }

    public String generateBackendDirectRoutingBindlingKey(String project){
        return String.format("berk_%s", project);
    }

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

    public ContainerExchange getContainerExchange() {
        return containerExchange;
    }

    public void setContainerExchange(ContainerExchange containerExchange) {
        this.containerExchange = containerExchange;
    }
}
