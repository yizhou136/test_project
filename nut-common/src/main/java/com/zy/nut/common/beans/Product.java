package com.zy.nut.common.beans;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/30.
 */
public class Product implements Serializable {
    private long pid;
    private String name;


    private long ctime;


    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }
}
