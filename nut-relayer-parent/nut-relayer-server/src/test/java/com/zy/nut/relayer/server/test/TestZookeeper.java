package com.zy.nut.relayer.server.test;

import org.apache.zookeeper.*;

import java.io.IOException;

/**
 * Created by zhougb on 2016/11/7.
 */
public class TestZookeeper {


    public static void main(String argv[]){
        int sessionTimeout = 10*1000;
        Watcher watcher = new Watcher() {
            public void process(WatchedEvent watchedEvent) {
                System.out.println(watchedEvent);
            }
        };
        try {
            ZooKeeper zooKeeper = new ZooKeeper("192.168.5.209:2181", sessionTimeout, watcher);
            System.out.println("sessionId:"+zooKeeper.getSessionId());
            String res = zooKeeper.create("/workers", "worker desc".getBytes(), null, CreateMode.EPHEMERAL_SEQUENTIAL);
            System.out.println("res:"+res);

            zooKeeper.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }
}
