package com.zy.nut.web.test.java;

/**
 * Created by zhougb on 2016/12/8.
 */
public class My implements InterfaceI, InterfaceJ{

    @Override
    public void f(String name) {
        //InterfaceJ.super.f(name);
        InterfaceI.super.f(name);
        //System.out.println("print My f");


        InterfaceJ interfaceI  = () ->{return false;};
    }

    @Override
    public boolean t() {
        return false;
    }
}
