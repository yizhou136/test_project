package com.zy.nut.web.test.kryo;

/**
 * Created by zhougb on 2017/3/2.
 */
public class N {
    private int a;
    private String b;
    private int c;

    private PersoN persoN;

    public N(){}

    public N(int a, String b, int c){
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

    public PersoN getPersoN() {
        return persoN;
    }

    public void setPersoN(PersoN persoN) {
        this.persoN = persoN;
    }
}
