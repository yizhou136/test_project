package com.zy.nut.web.test.kafka;

import org.apache.kafka.clients.producer.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by zhougb on 2016/12/20.
 */
public class TestKafka extends BaseKafka{

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

    public static void genMsg() throws ExecutionException, InterruptedException {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.5.43:9093");
        props.put("acks", "all");
        props.put("retries", 1);
        props.put("advertised.host.name", "192.168.5.43:9092");
        props.put("partitioner.class", MyPartitioner.class.getName());
        //props.put("batch.size", 16384);
        //props.put("linger.ms", 1);
        //props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        String topicName = GLOBAL_TOPIC_NAME;
        Producer  producer = new KafkaProducer(props);
        for (int i=0;i<10;i++){
            Future<RecordMetadata> future =
                    producer.send(new ProducerRecord(topicName, String.valueOf(i), String.valueOf(i)));
            RecordMetadata recordMetadata = future.get();
            System.out.print(recordMetadata);
        }
        producer.close();
    }

    public static void main(String argv[]){

        try {
            genMsg();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
