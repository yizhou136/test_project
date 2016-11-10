package com.zy.nut.relayer.common.amqp;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.QueueingConsumer;
import com.zy.nut.relayer.common.configure.Configuration;
import com.zy.nut.relayer.common.remoting.exchange.TransfredData;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
 * Created by zhougb on 2016/11/10.
 */
public class ServerendAMQPClient extends AbstractAMQPClient{

    public ServerendAMQPClient(Configuration configuration){
        super(configuration);
    }


    /*public void declares(String project){
        String fanoutExchangeName = generateFanoutExchangeName();
        String topicExchangeName = generateTopicExchangeName();
        String recvQueueName = generateRecvQueueName(project);
        String sendQueueName = generateSendQueueName(project);

        boolean durable = false;
        boolean autoDelete = false;
        Map<String, Object> arguments = null;
        Channel channel = generateChannel();
        try {
            AMQP.Queue.DeclareOk recvDeclareOk = channel.queueDeclare(recvQueueName, false, false, false, null);
            AMQP.Queue.DeclareOk sendDeclareOk = channel.queueDeclare(sendQueueName, false, false, false, null);

            AMQP.Exchange.DeclareOk fanoutExchangeDeclareOk = channel.exchangeDeclare(fanoutExchangeName,
                    "fanout", durable, autoDelete, arguments);
            AMQP.Exchange.DeclareOk topicExchangeDeclareOk = channel.exchangeDeclare(topicExchangeName,
                    "topic", durable, autoDelete, arguments);
            channel.queueBind(recvQueueName,fanoutExchangeName,null);
            channel.queueBind(recvQueueName,topicExchangeName,generateTopicBindlingKey(project));


        } catch (IOException e) {
            e.printStackTrace();
        }
    }*/

    public String generateRecvQueueName(String project){
        return String.format("recv_qu.%s.%s", getClusterName(), project);
    }

    public String generateFanoutExchangeName(){
        return String.format("%s_ex_fanout", getClusterName());
    }
    public String generateTopicExchangeName(){
        return String.format("%s_ex_topic", getClusterName());
    }

    public void declares(String project)throws IOException {
        Channel channel = generateChannel();
        //for receive
        String recvQueueName = generateRecvQueueName(project);
        AMQP.Queue.DeclareOk recvDeclareOk = channel.queueDeclare(recvQueueName, false, false, false, null);
        logger.info("recvDeclareOk:"+recvDeclareOk);


        //for send
        AMQP.Exchange.DeclareOk fanoutExchangeDeclareOk = channel.exchangeDeclare(fanoutExchangeName,
                "fanout", false, true, null);
        AMQP.Exchange.DeclareOk topicExchangeDeclareOk = channel.exchangeDeclare(topicExchangeName,
                "topic", false, true, null);
        /*channel.queueBind(recvQueueName,fanoutExchangeName,null);
        channel.queueBind(recvQueueName,topicExchangeName,generateTopicBindlingKey(project));*/
    }


    public void transformData(TransfredData transfredData, Set<String> clusterNames){
        boolean isFanout = clusterNames == null || clusterNames.isEmpty() ? true : false;
        String exchangeName = isFanout ? fanoutExchangeName : topicExchangeName;
        String routingKey  = isFanout ? "" : generateTopicRoutingKey(transfredData.getProject(),clusterNames);
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
            executorService.submit(new Runnable() {
                public void run() {
                    try {
                        Channel channel = generateChannel();
                        declares(project);

                        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
                        channel.basicConsume(generateRecvQueueName(project), false, queueingConsumer);

                        while (true){
                            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
                            System.out.println("log: "+new String(delivery.getBody()));
                            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);

                            System.out.println("reply to :"+delivery.getProperties().getReplyTo());
                            logger.info("AMQPReceiver delivery:"+delivery);
                            channel.basicPublish("", delivery.getProperties().getReplyTo(), null, "asfasdfas".getBytes());
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