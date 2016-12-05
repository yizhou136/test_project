package com.zy.nut.relayer.common.amqp;

import com.rabbitmq.client.*;
import com.zy.nut.relayer.common.configure.AMQPConf;
import com.zy.nut.relayer.common.configure.Cluster;
import com.zy.nut.relayer.common.configure.Configuration;
import com.zy.nut.relayer.common.container.ContainerExchange;
import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.remoting.exchange.TransformData;
import com.zy.nut.relayer.common.remoting.exchange.header.RelayerCodec;
import com.zy.nut.relayer.common.remoting.exchange.header.RelayerCodecSupport;

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
    private RelayerCodecSupport codecSupport;
    protected ExecutorService executorService = Executors.newCachedThreadPool();
    private ContainerExchange containerExchange;

    public AbstractAMQPClient(Configuration configuration){
        this(configuration, null);
    }

    public AbstractAMQPClient(Configuration configuration, ContainerExchange containerExchange){
        this.configuration = configuration;
        this.containerExchange = containerExchange;
        codecSupport = new RelayerCodecSupport();
        initAMQPReceiver();
    }

    public abstract void initAMQPReceiver();
    public abstract void transformData(TransformData transfredData);

    public void transformDataTo(Object msg, String matchConditions, Set<String> clusterNames){
        TransformData transformData = new TransformData();
        transformData.setProject(getDefaultProject());
        if (clusterNames == null || clusterNames.isEmpty()) {
            transformData.setExchangeType(TransformData.TRANSFORM_DATA_TYPE.FANOUT.getType());
            //transformData.setRoutingKey();
        }else if (clusterNames.size() == 1){
            String routingkey = clusterNames.iterator().next();
            transformData.setExchangeType(TransformData.TRANSFORM_DATA_TYPE.DIRECT.getType());
            transformData.setRoutingKey(routingkey);
        }else {
            String routingkey = genTopicRoutingKey("", clusterNames);
            transformData.setExchangeType(TransformData.TRANSFORM_DATA_TYPE.TOPIC.getType());
            transformData.setRoutingKey(routingkey);
        }

        transformData.setMatchConditiones(matchConditions);
        transformData.setData(msg);
        transformData(transformData);
    }

    public void declares(String project) throws IOException {
        boolean exculsive = false;
        boolean durable = false;
        boolean autoDelete = true;
        Channel channel = generateChannel();
        //declare for backend
        //for receive
        String backendRecvQueueName = genBackendRecvQueueName(project);
        AMQP.Queue.DeclareOk recvDeclareOk = channel.queueDeclare(backendRecvQueueName, durable, exculsive, autoDelete, null);
        logger.info("backend recvDeclareOk:"+recvDeclareOk);

        //for global send
        AMQP.Exchange.DeclareOk fanoutExchangeDeclareOk = channel.exchangeDeclare(
                genGlobalBackendFanoutEx(),
                "fanout", durable, autoDelete, null);
        AMQP.Exchange.DeclareOk topicExchangeDeclareOk = channel.exchangeDeclare(
                genGlobalBackendTopicEx(),
                "topic", durable, autoDelete, null);
        AMQP.Exchange.DeclareOk directExchangeDeclareOk = channel.exchangeDeclare(
                genGlobalBackendDirectEx(),
                "direct", durable, autoDelete, null);
        //for project send
        fanoutExchangeDeclareOk = channel.exchangeDeclare(
                genBackendFanoutEx(project),
                "fanout", durable, autoDelete, null);
        topicExchangeDeclareOk = channel.exchangeDeclare(
                genBackendTopicEx(project),
                "topic", durable, autoDelete, null);
        directExchangeDeclareOk = channel.exchangeDeclare(
                genBackendDirectEx(project),
                "direct", durable, autoDelete, null);
        /*channel.queueBind(recvQueueName,fanoutExchangeName,null);
        channel.queueBind(recvQueueName,topicExchangeName,generateTopicBindlingKey(project));*/


        //declare for server
        //for receive queue
        String serverRecvQueueName = genRelayerRecvQueueName(project, getClusterName());
        recvDeclareOk = channel.queueDeclare(serverRecvQueueName, durable, exculsive, autoDelete, null);
        logger.info("relayer recvDeclareOk:"+recvDeclareOk);

        //for server global send
        fanoutExchangeDeclareOk = channel.exchangeDeclare(
                genGlobalRelayerFanoutEx(),
                "fanout", durable, autoDelete, null);
        topicExchangeDeclareOk = channel.exchangeDeclare(
                genGlobalRelayerTopicEx(),
                "topic", durable, autoDelete, null);
        directExchangeDeclareOk = channel.exchangeDeclare(
                genGlobalRelayerDirectEx(),
                "direct", durable, autoDelete, null);
        //for server send
        fanoutExchangeDeclareOk = channel.exchangeDeclare(
                genRelayerFanoutEx(project),
                "fanout", durable, autoDelete, null);
        topicExchangeDeclareOk = channel.exchangeDeclare(
                genRelayerTopicEx(project),
                "topic", durable, autoDelete, null);
        directExchangeDeclareOk = channel.exchangeDeclare(
                genRelayerDirectEx(project),
                "direct", durable, autoDelete, null);

        //binding key for backend
        channel.exchangeBind(genBackendDirectEx(project), genGlobalBackendDirectEx(), genDirectRoutingBindlingKey(project));
        channel.exchangeBind(genBackendFanoutEx(project), genGlobalBackendFanoutEx(), "");
        channel.exchangeBind(genBackendTopicEx(project), genGlobalBackendTopicEx(), genTopicBindlingKey(project));

        channel.queueBind(backendRecvQueueName,
                genBackendTopicEx(project), genTopicBindlingKey(project));
        channel.queueBind(backendRecvQueueName,
                genBackendFanoutEx(project), "");
        channel.queueBind(backendRecvQueueName,
                genBackendDirectEx(project), genDirectRoutingBindlingKey(project));

        //binding key for server
        channel.exchangeBind(genRelayerTopicEx(project), genGlobalRelayerTopicEx(),
                genTopicBindlingKey(project+"_"+getClusterName()));
        channel.exchangeBind(genRelayerFanoutEx(project), genGlobalRelayerFanoutEx(), "");
        channel.exchangeBind(genRelayerDirectEx(project), genGlobalRelayerDirectEx(),
                genDirectRoutingBindlingKey(project+"_"+getClusterName()));

        channel.queueBind(serverRecvQueueName,
                genRelayerTopicEx(project), genTopicBindlingKey(getClusterName()));
        channel.queueBind(serverRecvQueueName,
                genRelayerFanoutEx(project), "");
        channel.queueBind(serverRecvQueueName,
                genRelayerDirectEx(project), genDirectRoutingBindlingKey(getClusterName()));
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
            connectionFactory.setVirtualHost("relayer");
            connectionFactory.setAutomaticRecoveryEnabled(true);
            connectionFactory.setNetworkRecoveryInterval(20);
            connectionFactory.setRequestedHeartbeat(500);
            Connection connection = connectionFactory.newConnection();
            connection.addShutdownListener(new ShutdownListener() {
                public void shutdownCompleted(ShutdownSignalException e) {
                    logger.error("shutdownCompleted xxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
                }
            });
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

    public byte getDefaultProject(){
        return (byte) 0;
    }

    /*public String getDefaultProject(){
        Set<String> projects = getConfiguration().getProjects();
        return projects.iterator().next();
    }*/

    public String genBackendRecvQueueName(String project) {
        return String.format("backend_recvq_%s", project);
    }

    public String genRelayerRecvQueueName(String project, String relayerClusterName) {
        return String.format("relayer_recvq_%s_%s", project, relayerClusterName);
    }

    /*public String generateBackendSendQueueName(String project){
        return generateServerRecvQueueName(project);
    }

    public String generateServerSendQueueName(String project){
        return generateBackendRecvQueueName(project);
    }*/

    public String genGlobalRelayerExchangeName(String type){
        return String.format("relayer_%s_ex", type);
    }

    public String genGlobalRelayerFanoutEx(){
        return genGlobalRelayerExchangeName("fanout");
    }
    public String genGlobalRelayerDirectEx(){
        return genGlobalRelayerExchangeName("direct");
    }
    public String genGlobalRelayerTopicEx(){
        return genGlobalRelayerExchangeName("topic");
    }

    public String genRelayerExchangeName(String project,String type){
        return String.format("relayer_%s_%s_ex", project, type);
    }

    public String genRelayerFanoutEx(String project){
        return genRelayerExchangeName(project, "fanout");
    }

    public String genRelayerDirectEx(String project){
        return genRelayerExchangeName(project,"direct");
    }

    public String genRelayerTopicEx(String project){
        return genRelayerExchangeName(project,"topic");
    }
///-----------------------------------------------------------------------------
    public String genGlobalBackendExchangeName(String type){
        return String.format("backend_%s_ex", type);
    }

    public String genGlobalBackendFanoutEx(){
        return genGlobalBackendExchangeName("fanout");
    }
    public String genGlobalBackendTopicEx(){
        return genGlobalBackendExchangeName("topic");
    }
    public String genGlobalBackendDirectEx(){
        return genGlobalBackendExchangeName("direct");
    }

    public String genBackendExchangeName(String project,String type){
        return String.format("backend_%s_%s_ex", project, type);
    }

    public String genBackendFanoutEx(String project){
        return genBackendExchangeName(project, "fanout");
    }

    public String genBackendDirectEx(String project){
        return genBackendExchangeName(project, "direct");
    }

    public String genBackendTopicEx(String project){
        return genBackendExchangeName(project, "topic");
    }

    public String genTopicBindlingKey(String key){
        return String.format("#.%s.#", key);
    }


    public String genTopicRoutingKey(String prefix, Set<String> keys){
        StringBuilder stringBuilder = new StringBuilder(prefix);
        for (String key:keys)
            stringBuilder.append(".").append(key);
        return stringBuilder.toString();
    }

    public String genDirectRoutingBindlingKey(String key){
        return String.format("dir_%s", key);
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

    public RelayerCodecSupport getCodecSupport() {
        return codecSupport;
    }

    public void setCodecSupport(RelayerCodecSupport codecSupport) {
        this.codecSupport = codecSupport;
    }
}
