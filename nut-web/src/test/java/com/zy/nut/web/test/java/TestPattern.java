package com.zy.nut.web.test.java;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhougb on 2017/2/28.
 */
public class TestPattern {
    private static final Pattern pattern = Pattern.compile("(.*?)_\\d+[x|X]\\d+");

    public static void main(String argvs[]){
        Matcher matcher = pattern.matcher("adfdsafasdf_120x90");
        if (matcher.find())
        System.out.println(matcher.group(1));

        System.out.println(isPower(4));
    }


    public static boolean isPower(int v){
        return  (v & -v) == v;
    }
}
