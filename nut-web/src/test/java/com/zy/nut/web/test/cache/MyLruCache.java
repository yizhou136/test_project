package com.zy.nut.web.test.cache;

import com.zy.nut.web.test.java.My;

import java.util.*;

/**
 * Created by zhougb on 2017/1/17.
 */
public class MyLruCache extends LinkedHashMap<String,Integer>{

    public MyLruCache(){
        super(10, 0.75f, true);
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<String, Integer> eldest) {
        return size() >= 4;
    }

    public static void print(MyLruCache myLruCache){
        myLruCache.forEach((k,v)->{
            System.out.print(k+",");
        });
        System.out.println();
    }

    public static void printCollection(Collection collection){
        collection.forEach(e->System.out.print(e+","));
        System.out.println();
    }

    public static void main(String argv[]){
        MyLruCache myLruCache = new MyLruCache();
        myLruCache.put("a", 1);
        myLruCache.put("b", 2);
        print(myLruCache);
        myLruCache.put("c", 3);
        print(myLruCache);
        myLruCache.put("d", 4);
        print(myLruCache);


        myLruCache.get("b");
        myLruCache.get("d");
        print(myLruCache);

        myLruCache.put("e",5);
        print(myLruCache);


        PriorityQueue<String> priorityQueue = new PriorityQueue<>(Comparator.reverseOrder());
        addPriority(priorityQueue, "1");
        printCollection(priorityQueue);
        addPriority(priorityQueue, "4");
        printCollection(priorityQueue);
        addPriority(priorityQueue, "3");
        printCollection(priorityQueue);
        addPriority(priorityQueue, "8");
        printCollection(priorityQueue);
        addPriority(priorityQueue, "6");
        printCollection(priorityQueue);
        addPriority(priorityQueue, "5");
        printCollection(priorityQueue);

        addPriority(priorityQueue, "2");
        printCollection(priorityQueue);
        //Strings a;
        //Exceptions a;
    }

    public static void addPriority(PriorityQueue<String> priorityQueue, String str){
        priorityQueue.add(str);
        if (priorityQueue.size() >= 5)
            priorityQueue.remove();
    }
}
