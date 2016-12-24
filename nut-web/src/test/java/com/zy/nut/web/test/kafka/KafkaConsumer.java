package com.zy.nut.web.test.kafka;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;

import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by zhougb on 2016/12/20.
 */
public class KafkaConsumer extends BaseKafka{
    public static void consumerMsg(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.5.43:9092");
        props.put("group.id","g1");
        props.put("enable.auto.commit","false");
        props.put("auto.commit.interval.ms","1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        org.apache.kafka.clients.consumer.KafkaConsumer consumer =
                new org.apache.kafka.clients.consumer.KafkaConsumer(props);
        consumer.subscribe(Stream.of(GLOBAL_TOPIC_NAME, "test1").collect(Collectors.toList()),
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
        try {
            while (true){
                ConsumerRecords consumerRecords = consumer.poll(Long.MAX_VALUE);
                for (TopicPartition topicPartition : (Set<TopicPartition>)consumerRecords.partitions()){
                    List<ConsumerRecord> partitionRecords = consumerRecords.records(topicPartition);
                    for (ConsumerRecord record : partitionRecords) {
                        //可以自定义Handler,处理对应的TOPIC消息(partitionRecords.key())
                        System.out.println(record.offset() + " "+ record.key()
                                +" : " + record.value()
                                +" tm:"+record.timestamp()
                                +" p:"+record.partition());
                    }
                    consumer.commitSync();//同步
                }
            }
        }finally {
            consumer.close();
        }
    }

    public static void main(String argv[]){
        consumerMsg();
    }
}
