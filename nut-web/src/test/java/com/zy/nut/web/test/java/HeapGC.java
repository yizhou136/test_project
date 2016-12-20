package com.zy.nut.web.test.java;

import java.util.List;
import java.util.ArrayList;
/**
 * Created by zhougb on 2016/12/12.
 */
public class HeapGC {
    static class OOMObject{}

    public static void main(String argv[]){
        List<OOMObject> list = new ArrayList<OOMObject>();
        while (true){
            list.add(new OOMObject());
        }
    }
}
