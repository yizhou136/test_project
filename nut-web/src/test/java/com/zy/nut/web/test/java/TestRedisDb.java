package com.zy.nut.web.test.java;

import com.alibaba.dubbo.common.logger.Logger;
import com.alibaba.dubbo.common.logger.LoggerFactory;
import com.alibaba.dubbo.common.utils.LRUCache;
import com.zy.nut.common.beans.Product;
import com.zy.nut.common.beans.User;

import java.time.Instant;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;

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
            RedisMiss<Product> redisMiss = redisMissMap.computeIfAbsent(pid, (id)->{
                logger.info("computeIfAbsent    xxxxxxxxxxxxxxx thread:"+Thread.currentThread().getName());
                return new RedisMiss<Product>();
            });
            int waitThread = 0;
            if ((waitThread=redisMiss.inc()) == 0){
                logger.info("loadFromRedis is null and loadFromDB thread:"+Thread.currentThread().getName());
                product = loadFromDB(pid);
                ///logger.info("loadFromRedis is null and loadFromDB product:"+product+" thread:"+Thread.currentThread().getName());
                if (product != null){
                    redisMiss.loadDataSuccessed(product);
                    saveToRedis(product);
                }else {
                    redisMiss.loadDataFailured();
                }
            }else {
                logger.info("waitForLoadData thread:"+Thread.currentThread().getName()+" waitThread:"+waitThread);
                product = redisMiss.waitForLoadData();

                logger.info("waitForLoadData and returned thread:"+Thread.currentThread().getName());
            }
        }else {
            logger.info("loadFromRedis and return thread:"+Thread.currentThread().getName());
        }

        return product;
    }


    public Product loadFromRedis(Long pid){
        try {
            Thread.sleep(random.nextInt(300));
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
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Product product = new Product();
        product.setPid(pid);
        product.setCtime(System.currentTimeMillis());

        logger.info("xxxxxxxxxxxxxxxx loadFromDB thread:"+Thread.currentThread().getName());
        return product;
    }

    public static void main(String argv[]){
        TestRedisDb testRedisDb = new TestRedisDb();
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i=0; i < 2; i++) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    //logger.info("run Thread:"+Thread.currentThread().getName());
                    testRedisDb.load(Long.valueOf(1));
                }
            };
            //executorService.execute(runnable);
            //Future future = executorService.submit(runnable);
        }


        RedisMiss<Product> redisMiss0 = new RedisMiss<Product>();
        logger.info("redisMiss0:"+redisMiss0);

        testRedisDb.redisMissMap.put(Long.valueOf(1), redisMiss0);

        RedisMiss<Product> redisMiss = testRedisDb.redisMissMap.merge(Long.valueOf(1),
                redisMiss0, (existv, newv)->{
                    logger.info("merge existv:"+existv+" newv:"+newv);
            return new RedisMiss();
        });

        logger.info("after redisMiss:"+redisMiss);
    }
}
