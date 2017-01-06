package com.zy.nut.web.test.java;

import java.util.concurrent.*;

/**
 * Created by zhougb on 2017/1/4.
 */
public class TestExecutorService {
    private static final ExecutorService executorService = Executors.newCachedThreadPool();
    private static final CountDownLatch countDownLatch = new CountDownLatch(2);

    public static void main(String argv[]){
        Future future = executorService.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("submit run1");
                try {
                    if (true){
                        throw new RuntimeException("submit error hehehe");
                    }
                }finally {
                    countDownLatch.countDown();
                }

                System.out.println("submit run2");
            }
        });

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("execute run1");
                try {
                    if (true){
                        throw new RuntimeException("execute error hehehe");
                    }
                }finally {
                    countDownLatch.countDown();
                }

                System.out.println("execute run2");
            }
        });

        try {
            countDownLatch.await();
            System.out.println("run end");
            //System.out.println(future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        executorService.shutdown();
    }
}
