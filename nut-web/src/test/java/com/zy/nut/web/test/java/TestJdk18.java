package com.zy.nut.web.test.java;

import com.google.common.base.*;
import com.google.common.collect.Collections2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

/**
 * Created by zhougb on 2016/11/28.
 */
public class TestJdk18 {

    public static Runnable addThen(Runnable r1, Runnable r2){
        return new Runnable(){
            @Override
            public void run() {
                r1.run();
                r2.run();
            }
        };
    }

    public static void testPriorityQueue(){
        List<Integer> list = new ArrayList<>();
        list.sort(Integer::compareTo);
        list.sort(Comparator.naturalOrder());

        PriorityQueue<Integer> priorityQueue = new PriorityQueue<Integer>(10, Integer::compareTo);
        priorityQueue.add(1);
        priorityQueue.add(4);
        priorityQueue.add(2);
        priorityQueue.add(6);
        priorityQueue.add(3);
        priorityQueue.add(9);
        priorityQueue.add(8);


        priorityQueue.removeIf((v)->{return v>10;});

        System.out.println("head:"+priorityQueue.peek());
        while (priorityQueue.size() > 5){
            priorityQueue.remove();
        }

        int size = priorityQueue.size();
        for (int i=0;i<size;i++){
            System.out.println(priorityQueue.poll());
        }

        System.out.println(priorityQueue.peek());
    }

    public static void main(String argv[]){
        testPriorityQueue();
        Runnable runnable1 = () -> {System.out.println("runnable1");};
        Runnable runnable2 = () -> {System.out.println("runnable2");};

        int intArr[] = new int[]{1,3,4,7,2};
        Arrays.sort(intArr);
        List<Integer> intList = Arrays.asList(1,8,2,5,3,7,4);
        Collections.sort(intList);

        List<Person> personList = Arrays.asList(new Person(1,"zy"));
        personList.sort(comparing(Person::getId));
        //intList.sort(Comparator.comparing());

        /*for (int i:intArr)
            intList.add(()->{System.out.println(i); return i;});*/


        addThen(runnable1, runnable2).run();

        My my = new My();
        my.f("sadf");

        long cnt = intList.parallelStream().filter((i)->i<5).count();
        System.out.println("cnt:"+cnt);

        Stream<String> stream = Stream.of("abc","efg");
        stream.filter((s)->s.indexOf("a") != -1).count();

        Path path = Paths.get("E:\\workspace\\web\\test_project\\","readme.txt");
        try(Stream<String> line = Files.lines(path)){
            line.forEach((s)->System.out.println(s));
            //System.out.println(line);
        } catch (IOException e) {
            e.printStackTrace();
        }


        Stream<Double>  randomStream = Stream.generate(Math::random).limit(10);
        System.out.println(randomStream.findAny().get());
        /*System.out.println(randomStream.findAny().get());
        System.out.println(randomStream.findAny().get());
        System.out.println(randomStream.findAny().get());*/

        Double objects[] = Stream.iterate(1.0, p -> p *2)
                .peek(e->System.out.println(e))
                .limit(30).toArray(Double[]::new);
        System.out.println(Arrays.toString(objects));

        Stream<String> s = Stream.of("a a b b c".split(" ")).distinct();
        System.out.println(Arrays.toString(s.toArray()));

        Stream<String> sourt = Stream.of("a a b b c".split(" "))
                .distinct()
                .sorted(comparing(String::length).reversed());
        System.out.println(Arrays.toString(sourt.toArray()));

        Optional<String> optional = Stream.of("a","c","e","ab","e").filter((a)-> a.equals("ad")).findAny();
        optional.ifPresent(System.out::print);
        String val = optional.orElse("null");
        System.out.println("v:"+val);

        Optional optional1 = Optional.of(val);
        optional1 = optional.ofNullable(val);
        //optional1.flatMap()


        Stream.of(1,3).mapToInt(i->{return (int)i;});
        IntStream.of(3,4,5,63);

        Optional<Integer> sumOptional = Stream.of(1,3).reduce((x,y)->x+y);
        sumOptional.ifPresent(System.out::println);
        Stream.of(1,3,4).reduce(0, Integer::sum);

        Map<Integer,String> map = Stream.of(new Person(1,"zy1"),
                new Person(2,"zy2"),
                new Person(3,"zy3"),
                new Person(4,"zy4")).collect(Collectors.toMap(Person::getId, Person::getName));
        System.out.println("map:"+map);

        Map<String,List<Locale>> locales = Stream.of(Locale.getAvailableLocales())
                .collect(Collectors.groupingBy(Locale::getCountry));

        Map<Boolean,Set<Locale>> locales2 = Stream.of(Locale.getAvailableLocales())
                .collect(Collectors.partitioningBy(locale -> locale.getLanguage().contains("en"),
                        Collectors.toSet()));

        System.out.println("map:"+locales2);


        IntStream intStream = IntStream.rangeClosed(0, 3);
        intStream.reduce(Integer::sum).ifPresent(System.out::print);

        Function<String,Integer>  fun = String::length;

        System.out.println("\nmapxxxxx:");
        Random random = new Random();
        IntStream intStream1 = random.ints();
        intStream1.findAny().ifPresent(System.out::print);


        Map<String,Integer> wordCntMap = Stream.of("a,b,c,d,aa,cc,a,b,c,d,c".split(","))
                .parallel().collect(Collectors.toMap((k)->{return k;},
                        (v)->{
                            try {
                                Thread.sleep(30);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            return 1;},
                        (newex,old)->{return old+1;}));

        System.out.println("\nwordCntMap:"+wordCntMap);


        Map<String,Integer> topWordCntMap = Stream.of("aadfa,badsfasd,cadfas,dasdfas,aaasdfas,ccasdfasdf,aasdf,basdf,c,d,c"
                .split(",")).filter(ss->ss.length()>=5).sorted(comparing(String::length))
                .parallel().collect(Collectors.toConcurrentMap((k)->{return k;},
                        (v)->{
                            return 1;},
                        (newex,old)->{return old+1;}));
        ConcurrentHashMap<String,Integer> concurrentHashMap = (ConcurrentHashMap<String, Integer>)topWordCntMap;
        Integer key = concurrentHashMap.search(1, (k,v)->v>=1?v:null);
        Integer vv = concurrentHashMap.searchValues(1, (v)->v>=1?v:0);

        String keys = concurrentHashMap.searchEntries(1, (e)->e.getKey());
        System.out.println("\ntopWordCntMap:"+topWordCntMap);
        System.out.println("\nconcurrentHashMap:"+concurrentHashMap+" key:"+key +" vv:"+vv+" keys:"+keys);

        concurrentHashMap.forEach(1, (k,v)->System.out.println(k+"-->"+v));
        concurrentHashMap.forEach(1, (k,v)->k+"-->"+v, System.out::print);

        int sum = concurrentHashMap.reduceValuesToInt(1, (i)->i,0, Integer::sum);

        Set<String> setStr = ConcurrentHashMap.newKeySet();
        setStr.add("adfasdfas");
        setStr.add("adfasdfas2");
        setStr.add("adfasdfas3");
        System.out.println("\nconcurrentHashMap sum values=:"+sum+" set:"+setStr);

        int [] intArray = new int[]{33,234,23414};
        //Arrays.parallelPrefix(intArray, (f,se)->f*se);
        Arrays.parallelSetAll(intArray, (f)->{System.out.println(f); return f*2;});
        System.out.println("intArray:"+Arrays.toString(intArray));

        Predicate<String> predicate = ss -> ss.length() >= 5;
        List<String> list = Stream.of("1234511,1234522,1234533,123,12345,12345,123453,123452,123451,12345,123456"
                .split(","))
                .peek((ss)->{System.out.print("before sorted::::"+ss);})
                .sorted(comparing(String::length)
                .reversed())
                .peek((ss)->{System.out.print("\nbefore limit::::"+ss);})
                .limit(5)
                .collect(Collectors.toList());

        System.out.println("\nsortedWordCntList:"+list);


        Objects obj;
        Logger logger;
        //SmoothRate
        //Collections2.
        com.google.common.base.Optional a;
    }
}