package com.zy.nut.web.test.java;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

/**
 * Created by zhougb on 2016/12/8.
 */
public class TestTime {

    public static void main2() {
        char x = 'A';
        int i = 0;
        System.out.print(true  ? x : 0);
        System.out.print(false ? i : x);
    }

    public static boolean isOdd(int i){
        System.out.println("xx:"+(i%2));
        //return i % 2 == 1;

        System.out.println(2.00 - 1.10);

        System.out.println(0.11);

        return (i & 1) != 0;
    }

    public static void main(String argv[]){
        Instant start = Instant.now();
        try {
            Thread.sleep(332);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Instant end = Instant.now();

        Duration duration = Duration.between(start, end);
        duration = duration.plusHours(1);
        System.out.println(duration.getSeconds()+" "+duration.getNano()+" "+duration.getUnits());

        Date date = new Date();


        isOdd(-4);

        main2();

        int x = 1 , i = 0;
        x += i;     // Must be LEGAL
        x = x + i;  // Must be ILLEGAL
    }
}
