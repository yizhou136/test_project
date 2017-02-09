package com.zy.nut.web.test.kafka;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.common.TopicPartition;
import org.apache.log4j.helpers.LogLog;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by zhougb on 2016/12/20.
 */
public class KafkaConsumer extends BaseKafka{
    static{
        LogLog.setInternalDebugging(true);
    }

    public static void consumerMsg(){
        Properties props = new Properties();
        props.put("bootstrap.servers", KAFKA_HOST);
        props.put("group.id","g1");
        props.put("enable.auto.commit","false");
        props.put("auto.commit.interval.ms","1000");
        props.put("session.timeout.ms", "15000");
        props.put("heartbeat.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        //props.put("group.min.session.timeout.ms", "1000");
        //props.put("group.max.session.timeout.ms", "40000");

        org.apache.kafka.clients.consumer.KafkaConsumer consumer =
                new org.apache.kafka.clients.consumer.KafkaConsumer(props);
        consumer.subscribe(Stream.of(GLOBAL_TOPIC_NAME, "mytest").collect(Collectors.toList()),
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
                    /*Long lastOffset = partitionRecords.listIterator().next().offset();

                    Map<TopicPartition, OffsetAndMetadata> m = Collections.singletonMap(topicPartition,new OffsetAndMetadata(lastOffset+1));
                    consumer.commitSync(m);*/
                }


                /*List<TopicPartition> list = new ArrayList<>();
                list.add(new TopicPartition("t_1_2_n",0));
                list.add(new TopicPartition("t_1_2_n",1));
                Map<TopicPartition,Long> map = consumer.beginningOffsets(list);
                map.forEach((k,v)->{
                    System.out.println("topicpartition:"+k+"  position:"+v);
                });
                consumer.seek(list.get(0), map.get(list.get(0)));
                consumer.seek(list.get(1), map.get(list.get(1)));*/
            }
        }finally {
            consumer.close();
        }
    }

    public static void main(String argv[]){
        consumerMsg();
    }
}
