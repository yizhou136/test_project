package com.zy.nut.web.test.java;

import java.net.URL;
import java.util.concurrent.*;

/**
 * Created by zhougb on 2016/12/9.
 */
public class TestFuture {
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    public static Future<String> getContent(URL url){
        Future<String> future = executorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Thread.sleep(330);
                return Thread.currentThread().getName();
            }
        });

        return future;
    }


    public static String blockingGetConntent() throws InterruptedException {
        Thread.sleep(323);
        return Thread.currentThread().getName();
    }


    public static CompletableFuture<String> getContent2(URL url){
        CompletableFuture<String> future = CompletableFuture.supplyAsync(
                () -> {
                    try {
                        System.out.println("blocking get content");
                        return blockingGetConntent();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
        );

        return future.thenCompose((str)->{return CompletableFuture.completedFuture(str);})
                .thenApply((s)->{System.out.println("xxx:"+s);return s;});
    }

    public static void main(String argv[]) throws ExecutionException, InterruptedException {
        Future<String> future = getContent(null);
        System.out.println(future.get());


        CompletableFuture<String> c = getContent2(null);
        c.handle((st, th)->{System.out.println("xxxxxxxxxxxxx"); return null;});
        //c.thenAccept(System.out::println).get();

        executorService.shutdown();


    }
}
