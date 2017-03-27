package com.zy.nut.web.test.guava;

import com.google.common.cache.*;
import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;
import com.google.common.util.concurrent.RateLimiter;

import java.util.Comparator;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhougb on 2017/3/21.
 */
public class TestGuava {
    public static final Pattern pattern = Pattern.compile("\\w+");
    public static final String str = "A A B B c d e";

    public static void testLoadingCache(){
        LoadingCache<String,MyClass> loadingCache = CacheBuilder.newBuilder()
                .maximumSize(10)
                .expireAfterWrite(2, TimeUnit.SECONDS)
                .removalListener(new RemovalListener<String, MyClass>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, MyClass> notification) {
                        System.out.println("the myclass removed"+notification.getKey());
                    }
                })
                .build(new CacheLoader<String, MyClass>() {
                    @Override
                    public MyClass load(String key) throws Exception {
                        System.out.println("new Myclass");
                        return new MyClass(key);
                    }
                });
        try {
            MyClass myClass = loadingCache.get("abc");
            System.out.println(myClass.getName());
            Thread.sleep(3000);

            myClass = loadingCache.get("abc");
            System.out.println(myClass.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void testMuliSet(){
        Matcher matcher = pattern.matcher(str);
        Multiset<String> multiset = TreeMultiset.create(String::compareTo);
        while (matcher.find()){
            String word = matcher.group();
            multiset.add(word);
        }

        multiset.entrySet().forEach((e)->{System.out.println(e.getCount()+" "+e.getElement());});
    }

    public static void testRateLimit2(){
        System.out.println("SmoothingBrustWariningup");
        RateLimiter rateLimiter = RateLimiter.create(5, 1, TimeUnit.SECONDS);
        for (int i=0; i<12; i++) {
            System.out.println(rateLimiter.acquire());
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i=0; i<12; i++) {
            System.out.println(rateLimiter.acquire());
        }

        System.out.println("SmoothingBrust");
        rateLimiter = RateLimiter.create(5);
        for (int i=0; i<12; i++) {
            System.out.println(rateLimiter.acquire());
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i=0; i<12; i++) {
            System.out.println(rateLimiter.acquire());
        }
    }

    public static void testRateLimit(){
        final AtomicInteger atomicInteger = new AtomicInteger();
        final RateLimiter rateLimiter = RateLimiter.create(8);
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i=0;i<10;i++)
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    rateLimiter.acquire();
                    System.out.println("request:"+atomicInteger.incrementAndGet());
                }
            });

        executorService.shutdown();
    }

    public static void main(String argv[]){
        //testLoadingCache();

        testRateLimit2();

        //testMuliSet();
    }

    public static class MyClass{
        private String name;

        public MyClass(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
