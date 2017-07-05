package com.zy.nut.web.test.thread;

/**
 * @author by zy.
 */
public class Bitmap {

    private int idx;

    public Bitmap(int i){
        idx = i;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("bitmap:").append(idx);
        return stringBuilder.toString();
    }
}
