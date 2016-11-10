package com.zy.nut.relayer.common.amqp;

import com.rabbitmq.client.*;
import com.zy.nut.relayer.common.configure.Configuration;
import com.zy.nut.relayer.common.remoting.exchange.TransfredData;

import java.io.IOException;
import java.util.Set;

/**
 * Created by zhougb on 2016/11/10.
 */
public class BackendAMQPClient extends AbstractAMQPClient{

    public BackendAMQPClient(Configuration configuration){
        super(configuration);
    }


    public void transformData(TransfredData transfredData, Set<String> clusterNames){
        String project = transfredData.getProject();
        String exchangeName = null;
        String routingKey = null;
        if (clusterNames == null || clusterNames.isEmpty()) {
            exchangeName = generateServerFanoutExchangeName();
            routingKey = "";
        }else if (clusterNames.size() == 1){
            String clusterName = clusterNames.iterator().next();
            exchangeName = generateServerDirectExchangeName();
            routingKey = generateServerDirectRoutingBindlingKey(project, clusterName);
        }else {
            exchangeName = generateServerTopicExchangeName();
            routingKey = generateServerTopicRoutingKey(project,clusterNames);
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
            channel.basicPublish(exchangeName, routingKey, basicProperties, "".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.info("start send msg end");
        //channel.waitForConfirms();
        //channel.close();
    }

    public void initAMQPReceiver(){
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
                        channel.basicConsume(generateBackendRecvQueueName(project), false, queueingConsumer);

                        while (true){
                            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
                            System.out.println("log: "+new String(delivery.getBody()));
                            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                            System.out.println("reply to :"+delivery.getProperties().getReplyTo());
                            logger.info("AMQPReceiver delivery:"+delivery);
                            TransfredData transfredData = new TransfredData();
                            transformData(transfredData, null);
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