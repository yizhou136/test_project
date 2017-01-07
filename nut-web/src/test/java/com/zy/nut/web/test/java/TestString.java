package com.zy.nut.web.test.java;

import java.util.Arrays;
import java.util.Optional;
import java.util.OptionalInt;

/**
 * Created by zhougb on 2016/12/9.
 */
public class TestString {

    public static void testHugeStringGC(){
        String hugeStr = "abc=3,d=e,fa=34,gad=34,sfasd=34,fas=34,dfa=34,sdf=34,sadfas=34,dfasd=34,fasdfa=34,dsfas=34,dfdsaffasdfsdafdsaf=34,asdfsdafdsafdsf";
        String[] arrStr = hugeStr.split("34");
    }

    public static void main(String argv[]){
        testHugeStringGC();

        System.out.println(String.join(",", "a","b","c"));

        "abc".codePoints().forEach(System.out::println);

        System.out.println(Math.floorMod(13,-3)+" "+Math.nextDown(0.2)+" "+Math.nextUp(0.2));

        OptionalInt maxOptional = Arrays.asList("abc","bca","afc","adc","dbc").stream()
                .filter((str)->str.startsWith("a"))
                .mapToInt(String::length)
                .max();

        maxOptional.ifPresent(System.out::println);
    }
}
