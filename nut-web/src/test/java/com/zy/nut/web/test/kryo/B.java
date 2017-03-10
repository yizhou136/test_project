package com.zy.nut.web.test.kryo;

import com.esotericsoftware.kryo.serializers.TaggedFieldSerializer;

import java.io.Serializable;

/**
 * Created by zhougb on 2017/3/2.
 */
public class B implements Serializable{
    //@TaggedFieldSerializer.Tag(1)
    private int a;
    //@TaggedFieldSerializer.Tag(3)
    private String b;
    //@TaggedFieldSerializer.Tag(2)
    private int c;

    //private N n;
    public B(){}

    public B(int a, String b, int c){
        this.a = a;
        this.b = b;
        this.c = c;
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

    /*public N getN() {
        return n;
    }
    public void setN(N n) {
        this.n = n;
    }*/
}
