package com.zy.nut.web.test.kafka;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.Node;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by zhougb on 2016/12/20.
 */
public class MyPartitioner implements Partitioner{

    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        int count = cluster.partitionCountForTopic(topic);
        int partition = Integer.parseInt((String)key) % count;
        TopicPartition topicPartition = new TopicPartition(topic, partition);
        PartitionInfo partitionInfo = cluster.partition(topicPartition);
        System.out.println(String.format("topic:%s key:%s value:%s cluster:%s cout:%d",
                topic,key,value,cluster,count));
        Node node = partitionInfo.leader();
        if (node == null || node.isEmpty()) {
            partition++;
            System.out.println("skip invalid node "+ partitionInfo);
        }
        return partition;
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
