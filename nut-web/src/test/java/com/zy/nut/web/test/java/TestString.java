package com.zy.nut.web.test.java;

/**
 * Created by zhougb on 2016/12/9.
 */
public class TestString {

    public static void main(String argv[]){
        System.out.println(String.join(",", "a","b","c"));

        "abc".codePoints().forEach(System.out::println);

        System.out.println(Math.floorMod(13,-3)+" "+Math.nextDown(0.2)+" "+Math.nextUp(0.2));
    }
}
