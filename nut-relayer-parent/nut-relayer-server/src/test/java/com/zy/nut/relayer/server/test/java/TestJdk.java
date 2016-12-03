package com.zy.nut.relayer.server.test.java;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.ToIntFunction;

/**
 * Created by Administrator on 2016/11/29.
 */
public class TestJdk {

    public static void main(String argv[]){
        DoIt doIt = (str) -> System.out.println("show"+str);

        doIt.doIt("asfdasf");

        Fun<Long, Long> fun = (x) -> x + 1;
        System.out.println(((Fun<Long,Long>)((x) -> x + 1)).apply(Long.valueOf(3)));
        System.out.println(fun.apply(Long.valueOf(3)));

        ToIntFunction<Integer>  dd = i -> i+3;
        ThreadLocal<SimpleDateFormat> threadLocal = ThreadLocal.withInitial(()->new SimpleDateFormat("YYYY"));


        System.out.println(threadLocal.get()+" "+dd.applyAsInt(3));

        System.out.println(threadLocal.get().format(new Date()));


    }
}
