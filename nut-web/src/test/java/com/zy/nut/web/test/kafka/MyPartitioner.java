package com.zy.nut.web.test.kafka;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by zhougb on 2016/12/20.
 */
public class MyPartitioner implements Partitioner{

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        int count = cluster.partitionCountForTopic(topic);
        System.out.println(String.format("topic:%s key:%s value:%s cluster:%s cout:%d",
                topic,key,value,cluster,count));
        return Integer.parseInt((String)key) % count;
    }

    @Override
    public void close() {
        System.out.println("MyPartitioner close");
    }

    @Override
    public void configure(Map<String, ?> configs) {
        System.out.println("MyPartitioner configure:"+configs);
    }
}
