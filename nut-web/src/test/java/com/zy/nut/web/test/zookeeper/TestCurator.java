package com.zy.nut.web.test.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
 * Created by Administrator on 2016/12/17.
 */
public class TestCurator {

    public static void main(String argv[]) throws Exception {
        String  connectionString = "192.168.5.62:4180";

        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(connectionString)
                .retryPolicy(new ExponentialBackoffRetry(500, 3))
                .build();
        curatorFramework.start();

                //CuratorFrameworkFactory.newClient(connectionString, )
        String path = "/flume/a1";
        String data = "a1.sources=r1\n" +
                "a1.sinks = k1\n" +
                "a1.channels = c1\n" +
                "\n" +
                "\n" +
                "a1.sources.r1.type = netcat\n" +
                "a1.sources.r1.bind = localhost\n" +
                "a1.sources.r1.port = 5555\n" +
                "\n" +
                "a1.sinks.k1.type = logger\n" +
                "\n" +
                "a1.channels.c1.type = memory\n" +
                "a1.channels.c1.capacity = 1000\n" +
                "a1.channels.c1.transactionCapacity = 100\n" +
                "\n" +
                "a1.sources.r1.channels = c1\n" +
                "a1.sinks.k1.channel = c1";
        Stat stat = curatorFramework.checkExists().forPath(path);
        if (stat != null)
            curatorFramework.delete().forPath(path);
        String result = curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path, data.getBytes());

        System.out.println("createPath res:"+result);

        byte [] bytes = curatorFramework.getData().forPath(path);

        System.out.println("getdata bytes:"+new String(bytes));

        //curatorFramework.getChildren().forPath("");
    }
}
