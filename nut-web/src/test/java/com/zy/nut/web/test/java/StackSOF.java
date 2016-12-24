package com.zy.nut.web.test.java;

/**
 * Created by zhougb on 2016/12/12.
 */
public class StackSOF {
    private double i;

    public  void stack(){
        i++;
        stack();
    }

    public void oom(){

    }

    public static void main(String argv[]){
        StackSOF stackSOF = new StackSOF();
        //stackSOF.stack();
        while (true)
            stackSOF.oom();
    }
}
