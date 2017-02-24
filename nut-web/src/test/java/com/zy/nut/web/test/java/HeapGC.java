package com.zy.nut.web.test.java;

import com.google.common.base.Splitter;
import com.google.common.collect.*;

import java.util.*;

/**
 * Created by zhougb on 2016/12/12.
 */
public class HeapGC {
    static OOMObject oo = new OOMObject();
    static {
        System.out.println("static initliter");
    }

    static class OOMObject{ OOMObject(){System.out.println("consuotor oomobject");}}

    public static void main(String argv[]){
        List<OOMObject> list = new ArrayList<OOMObject>();
        /*while (true){
            list.add(new OOMObject());
        }*/

        List<String> l = Lists.asList("1",new String[]{"2"});
        System.out.println(l);


        Iterable<String> words = Splitter.on(" ").split("we are family. we are family.");
        Multiset<String> counts = HashMultiset.create(words);


        System.out.println(counts.count("we"));
        System.out.println(counts.add("haha",3));
        System.out.println(counts.remove("we",1));
        System.out.println(counts);
        counts.forEach(System.out::print);


        Multimap<String,String>  multimap = ArrayListMultimap.create();
        multimap.put("joy","zhouyi");
        multimap.put("joy","wangwu");
        multimap.put("joy","lihao");

        System.out.println(multimap.get("joy"));

        Map<String,String> map = new HashMap<>();
        map.put("a","1");
        map.put("b","2");
        map.put("c","3");
        map.put("d","4");
        map.put(null, "5");



        BiMap<Integer,String> logfileMap = HashBiMap.create();
        logfileMap.put(1,"a.log");
        logfileMap.put(2,"b.log");
        logfileMap.put(3,"c.log");
        System.out.println("logfileMap:"+logfileMap);
        BiMap<String,Integer> filelogMap = logfileMap.inverse();
        System.out.println("filelogMap:"+filelogMap);
    }
}
