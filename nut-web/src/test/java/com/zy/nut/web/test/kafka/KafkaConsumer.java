package com.zy.nut.web.test.kafka;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;

import java.time.Instant;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by zhougb on 2016/12/20.
 */
public class KafkaConsumer extends BaseKafka{
    public static final Pattern pattern = Pattern.compile("channel_\\d+");

    public static void consumerMsg(){
        Properties props = new Properties();
        props.put("bootstrap.servers", GLOBAL_HOST);
        props.put("group.id","g1");
        props.put("enable.auto.commit","false");
        props.put("fetch.max.wait.ms",500);
        props.put("auto.offset.reset","earliest");
        /*props.put("auto.commit.interval.ms","1000");
        props.put("session.timeout.ms", "15000");
        props.put("heartbeat.interval.ms", "1000");
        props.put("group.min.session.timeout.ms", "1000");
        props.put("group.max.session.timeout.ms", "40000");*/
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        System.out.println("group.id __consumer_offset partition:"
                +Math.abs(props.getProperty("group.id").hashCode()) % 50);

        org.apache.kafka.clients.consumer.KafkaConsumer consumer =
                new org.apache.kafka.clients.consumer.KafkaConsumer(props);
        List<String> list = Stream.of(GLOBAL_TOPIC_NAME).collect(Collectors.toList());
        //list = Stream.of("t_*").collect(Collectors.toList());

        consumer.subscribe(list,
                new ConsumerRebalanceListener() {
                    @Override
                    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
                        System.out.print("onPartitionsRevoked partitions:");
                        partitions.stream().forEach((p)->{
                            System.out.print(" "+p);
                        });
                        System.out.println();
                    }

                    @Override
                    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
                        System.out.print("onPartitionsAssigned partitions:");
                        partitions.stream().forEach((p)->{
                            System.out.print(" "+p);
                        });
                        System.out.println();
                    }
                });
        //consumer.unsubscribe();
        try {
            while (true){
                //consumer.wakeup();
                ConsumerRecords consumerRecords = consumer.poll(Long.MAX_VALUE);
                System.out.println("after poll "+ Instant.now());
                for (TopicPartition topicPartition : (Set<TopicPartition>)consumerRecords.partitions()){
                    List<ConsumerRecord> partitionRecords = consumerRecords.records(topicPartition);
                    for (ConsumerRecord record : partitionRecords) {
                        //可以自定义Handler,处理对应的TOPIC消息(partitionRecords.key())
                        System.out.println("offset:"+record.offset() + " k:"+ record.key()
                                +" v:" + record.value()
                                +" tm:"+record.timestamp()
                                +" partition:"+record.partition());
                    }
                    //consumer.
                    //consumer.commitSync();//同步
                }
                /*new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("after print begin to commitAsync;"+ Instant.now());
                        consumer.commitAsync();
                        consumer.com
                    }
                }).start();*/
            }
        }finally {
            consumer.close();
        }
    }

    public static void main(String argv[]){
        Matcher matcher = pattern.matcher("channel_123");
        System.out.println(matcher.matches());
        consumerMsg();
    }
}
