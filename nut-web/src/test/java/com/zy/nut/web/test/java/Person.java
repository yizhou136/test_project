package com.zy.nut.web.test.java;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;

/**
 * Created by zhougb on 2016/12/8.
 */
public class Person {
    //@TaggedFieldSerializer.Tag(1)
    private int a;
    //@TaggedFieldSerializer.Tag(3)
    private String b;
    private int c;

    public Person(){}
    public Person(int id, String name){
        this.a = id;
        this.b = name;
        c  = 10;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }

    public String getB() {
        return b;
    }

    public void setB(String b) {
        this.b = b;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }
}
