package com.zy.nut.web.test.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Created by zhougb on 2016/12/20.
 */
public class TestKafka {

    /*private static class MyTest extends Thread{
        @Override
        public void run() {
            super.run();
        }

        public Producer createProducer(){
            Map<String,String> properties = new HashMap<String,String>();
            properties.put("","");

            return new Producer(ProducerConfig.addSerializerToConfig());
        }
    }*/

    public static void genMsg(){
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.5.43:9092");
        props.put("acks", "all");
        props.put("retries", 1);
        props.put("advertised.host.name", "192.168.5.43:9092");
        props.put("partitioner.class", MyPartitioner.class.getName());
        //props.put("batch.size", 16384);
        //props.put("linger.ms", 1);
        //props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        String topicName = "mymytopic2";
        Producer  producer = new KafkaProducer(props);
        for (int i=0;i<10;i++){
            producer.send(new ProducerRecord(topicName, String.valueOf(i), String.valueOf(i)));
        }
        producer.close();
    }

    public static void main(String argv[]){
        genMsg();
    }
}
