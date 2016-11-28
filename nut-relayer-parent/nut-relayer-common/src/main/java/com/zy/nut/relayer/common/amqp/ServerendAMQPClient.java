package com.zy.nut.relayer.common.amqp;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.zy.nut.relayer.common.configure.Configuration;
import com.zy.nut.relayer.common.container.ContainerExchange;
import com.zy.nut.relayer.common.remoting.exchange.TransformData;
import com.zy.nut.relayer.common.utils.StringUtils;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Created by zhougb on 2016/11/10.
 */
public class ServerendAMQPClient extends AbstractAMQPClient{

    public ServerendAMQPClient(Configuration configuration){
        super(configuration);
    }

    public ServerendAMQPClient(Configuration configuration, ContainerExchange containerExchange){
        super(configuration, containerExchange);
    }

    public void transformData(TransformData transfredData){
        String project = transfredData.getProject();
        byte   forwardtype = transfredData.getExchangeType();
        String exchangeName = null;
        String routingKey = null;
        if (StringUtils.isEmpty(project) || "all".equals(project)) {
            if (forwardtype == TransformData.TRANSFORM_DATA_TYPE.DIRECT.getType()) {
                //exchangeName = genGlobalBackendFanoutEx()
            }else if (forwardtype == TransformData.TRANSFORM_DATA_TYPE.TOPIC.getType()){
                exchangeName = genGlobalBackendTopicEx();
                routingKey = transfredData.getRoutingKey();
                if (StringUtils.isEmpty(routingKey)) {
                    Set<String> set = new LinkedHashSet<String>();
                    set.add("all");
                    routingKey = genTopicRoutingKey("", set);
                }
            }else if (forwardtype == TransformData.TRANSFORM_DATA_TYPE.FANOUT.getType()){
                exchangeName = genGlobalBackendFanoutEx();
            }
        }else if (forwardtype == TransformData.TRANSFORM_DATA_TYPE.DIRECT.getType()){
            exchangeName = genBackendDirectEx(project);
            routingKey = transfredData.getRoutingKey();//genDirectRoutingBindlingKey(project);
            if (StringUtils.isEmpty(routingKey))
                routingKey = genDirectRoutingBindlingKey(project);
        }else if (forwardtype == TransformData.TRANSFORM_DATA_TYPE.TOPIC.getType()){
            exchangeName = genBackendTopicEx(project);
            routingKey = transfredData.getRoutingKey();//genDirectRoutingBindlingKey(project);
            if (StringUtils.isEmpty(routingKey)) {
                Set<String> set = new LinkedHashSet<String>();
                set.add(project);
                routingKey = genTopicRoutingKey("", set);
            }
        }else if (forwardtype == TransformData.TRANSFORM_DATA_TYPE.FANOUT.getType()){
            exchangeName = genBackendFanoutEx(project);
            //routingKey = transfredData.getMatchConditiones();//genDirectRoutingBindlingKey(project);
        }

        Channel channel = generateChannel();
        /*channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(String.format("replyCode:%d, replyText:%s, exchange:%s, routingKey:%s, properties, body:%s",
                        replyCode, replyText, exchange, routingKey, new String(body)));
            }
        });
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println(String.format("handleAck deliveryTag:%d multiple:%s",
                        deliveryTag, String.valueOf(multiple)));
            }

            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println(String.format("handleNack deliveryTag:%d multiple:%s",
                        deliveryTag, String.valueOf(multiple)));
            }
        });
        AMQP.Confirm.SelectOk selectOk = channel.confirmSelect();
        logger.info("start send msg  selectOk:"+selectOk);*/


        AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties.Builder();
        //basicProperties = basicProperties.builder().deliveryMode(MessageDeliveryMode.toInt(MessageDeliveryMode.PERSISTENT)).build();
        //builder.deliveryMode(MessageDeliveryMode.toInt(MessageDeliveryMode.PERSISTENT));

        AMQP.BasicProperties basicProperties = builder.build();
        //channel.basicPublish(TestAmqp.TopicExchangerName, "haha", true, basicProperties, generateMsg().getBytes());
        //channel.basicPublish(TestAmqp.ExchangerName, "haha", true, basicProperties, generateMsg().getBytes());
        //channel.basicPublish(TestAmqp.ExchangerName, "haha2", true, basicProperties, generateMsg().getBytes());
        try {
            byte[] data = getCodecSupport().encode(transfredData);
            channel.basicPublish(exchangeName, routingKey, basicProperties, data);
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("start send msg end");
        //channel.waitForConfirms();
        //channel.close();
    }

    public void initAMQPReceiver(){
        if (getContainerExchange() == null)
            return;
        for(final String project:getConfiguration().getProjects()){
            try {
                declares(project);
            } catch (IOException e) {
                e.printStackTrace();
            }
            executorService.submit(new Runnable() {
                public void run() {
                    try {
                        Channel channel = generateChannel();
                        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
                        channel.basicConsume(genRelayerRecvQueueName(project, getClusterName()), false, queueingConsumer);

                        while (true){
                            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
                            byte[] data = delivery.getBody();
                            System.out.println("receive from backend:"+delivery.getEnvelope().getRoutingKey()
                               +" data.len:"+data.length);
                            channel.basicAck(delivery.getEnvelope().getDeliveryTag(),false);

                            getContainerExchange().receiveFromBackend(data);
                            //System.out.println("reply to :"+delivery.getProperties().getReplyTo());
                            /*logger.info("AMQPReceiver delivery:"+delivery);
                            TransformData transfredData = new TransformData();
                            transformData(transfredData);*/
                        }
                    }catch (InterruptedException ine){
                        ine.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}