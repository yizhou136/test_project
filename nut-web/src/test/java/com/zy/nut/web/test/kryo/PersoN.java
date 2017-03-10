package com.zy.nut.web.test.kryo;

import com.esotericsoftware.kryo.serializers.VersionFieldSerializer;
import com.esotericsoftware.kryo.serializers.VersionFieldSerializer.Since;

/**
 * Created by zhougb on 2016/12/8.
 */
public class PersoN {
    //@TaggedFieldSerializer.Tag(1)
    @VersionFieldSerializer.Since(1)
    private int a;
    //@TaggedFieldSerializer.Tag(3)
    private String b;
    private String b1;
    @VersionFieldSerializer.Since(3)
    private int c;

    private N n;
    private B bb;

    public PersoN(){}
    public PersoN(int id, String name){
        this.a = id;
        this.b = name;
        b1 = "ab";
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

    public N getN() {
        return n;
    }

    public void setN(N n) {
        this.n = n;
    }

    public B getBb() {
        return bb;
    }

    public void setBb(B bb) {
        this.bb = bb;
    }
}
