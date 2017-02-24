package com.zy.nut.web.test.java;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhougb on 2016/12/9.
 */
public class TestString {

    public static void testList(){
        System.out.println("testList");
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");

        Iterator<String> it = list.iterator();
        while (it.hasNext()){
            String str = it.next();
            System.out.println(str);
            //list.add("c");
            //list.remove("b");
            it.remove();
        }

        /*ListIterator<String> listIterator = list.listIterator();
        while (listIterator.hasNext()){
            String str = listIterator.next();
            listIterator.set("c");
        }*/

        System.out.println("testList after:");
        list.forEach((s)->{System.out.print(s);});
    }

    public static String extractUrlLastFileName(String url) {
        int lastSlashIdx = url.lastIndexOf('/');
        lastSlashIdx = -1;
        int lastDotIdx = url.indexOf('.', lastSlashIdx);
        if (lastDotIdx < 0) lastDotIdx = url.length();
        if (lastSlashIdx+1 > lastDotIdx) return "";
        return url.substring(lastSlashIdx + 1, lastDotIdx);
    }

    public static void main(String argv[]){
        /*String t = "http://q.qlogo.cn/qqapp/100528606/C7D2C4F02AEB11D58D7520547E6FD6DA/100";
        System.out.println(extractUrlLastFileName(t));
        System.out.println(String.join(",", "a","b","c"));
        "abc".codePoints().forEach(System.out::println);
        System.out.println(Math.floorMod(13,-3)+" "+Math.nextDown(0.2)+" "+Math.nextUp(0.2));
        System.out.println((-4%4));
        testList();
        int a = 10;
        int b = 10;
        methodA(a, b);
        System.out.println("a="+a);
        System.out.println("b="+b);*/

        /*ForkJoinPool forkJoinPool = new ForkJoinPool(3);
        forkJoinPool.submit(new MyRecursiveAction(0,9));
        System.out.println("put end");
        forkJoinPool.awaitQuiescence(0, TimeUnit.SECONDS);*/


        System.out.println( 10<<3 + 10<<1);
        System.out.println( 10 << 14);

        Integer i = 0;
        int b = 0;
        System.out.println(b == i);
    }
    public static void methodA(int a, int b){
        a = 1000;
        b = 2000;
    }



    private static class MyRecursiveAction extends RecursiveAction {
        private List<Integer> list = new ArrayList<>(10);
        private int start;
        private int end;
        public MyRecursiveAction(int start, int end){
            this.start = start;
            this.end = end;
        }
        @Override
        protected void compute() {
            //System.out.println("start:"+start+" end:"+end+" Thread:"+Thread.currentThread().getName());
            boolean canCompute = (end - start) == 0;
            if (canCompute){
                System.out.println("canCompute start:"+start+" end:"+end+" Thread:"+Thread.currentThread().getName());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }else {
                System.out.println("fork start:"+start+" end:"+end+" Thread:"+Thread.currentThread().getName());
                /*for (int i=0;i<10;i++){
                }*/
                int middel = (start+end) / 2;

                MyRecursiveAction leftMy = new MyRecursiveAction(start, middel);
                MyRecursiveAction rightMy = new MyRecursiveAction(middel+1, end);

                leftMy.fork();
                rightMy.fork();

                leftMy.join();
                rightMy.join();
            }
        }
    }
}
