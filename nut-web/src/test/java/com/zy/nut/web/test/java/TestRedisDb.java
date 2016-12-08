package com.zy.nut.web.test.java;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.LRUCache;
import com.zy.nut.common.beans.Product;
import com.zy.nut.common.beans.User;

import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Delayed;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2016/12/8.
 */
public class TestRedisDb {
    private static final Logger logger = LoggerFactory.getLogger(TestRedisDb.class);
    private Map<Long, Product>    redisDB = new ConcurrentHashMap<>();
    private Map<Long, RedisMiss> redisMissMap =  new ConcurrentHashMap<Long, RedisMiss>();
    private Random random = new Random();

    public Product load(Long pid){
        Product product = loadFromRedis(pid);
        if (product == null){
            logger.info("loadFromRedis is null");
            RedisMiss<Product> redisMiss = redisMissMap.putIfAbsent(pid, new RedisMiss<Product>());
            if (redisMiss.isDataValid()) return redisMiss.getData();
            int waitThreads = 0;
            if ((waitThreads=redisMiss.inc()) == 0){
                product = loadFromDB(pid);
                logger.info("loadFromDB has returned ");
                if (product != null){
                    logger.info("loadFromDB has loadDataSuccessed ");
                    redisMiss.loadDataSuccessed(product);
                    saveToRedis(product);
                }else {
                    logger.info("loadFromDB has loadDataFailured ");
                    redisMiss.loadDataFailured();
                }
            }else {
                logger.info("loadFromRedis is null and goto waitForLoadData waitThreads:"+waitThreads);
                product = redisMiss.waitForLoadData();
                logger.info("loadFromRedis is null and waitForLoadData has returnd ");
            }
        }else {
            logger.info("loadFromRedis and return");
        }

        return product;
    }


    public Product loadFromRedis(Long pid){
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (redisDB) {
            Product product =  redisDB.get(pid);
            if (product == null) return null;
            Instant instant = Instant.now();
            Instant loadedInstant = Instant.ofEpochMilli(product.getCtime());
            if (loadedInstant.isBefore(instant)){
                redisDB.remove(pid);
                return null;
            }else {
                return product;
            }
        }
    }


    public void saveToRedis(Product product){
        try {
            Thread.sleep(random.nextInt(500));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (redisDB) {
            redisDB.put(product.getPid(), product);
        }
    }

    public Product loadFromDB(Long pid){
        try {
            Thread.sleep(random.nextInt(1000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Product product = new Product();
        product.setPid(pid);
        product.setCtime(System.currentTimeMillis());
        return product;
    }

    public static void main(String argv[]){
        TestRedisDb testRedisDb = new TestRedisDb();
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i=0; i < 100; i++) {
            executorService.submit(new Runnable() {
                @Override
                public void run() {
                    testRedisDb.load(Long.valueOf(1));
                }
            });
        }
    }
}
