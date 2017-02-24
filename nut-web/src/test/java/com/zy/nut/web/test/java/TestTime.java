package com.zy.nut.web.test.java;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

/**
 * Created by zhougb on 2016/12/8.
 */
public class TestTime {

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


        int i1 = Integer.MAX_VALUE;
        System.out.println("max:"+Integer.MAX_VALUE);
        System.out.println("i1:"+(i1+1));

        int xx = -5  % 3;

        System.out.println("xx:"+(xx));
    }
}
