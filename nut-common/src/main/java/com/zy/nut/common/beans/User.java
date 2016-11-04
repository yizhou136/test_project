package com.zy.nut.common.beans;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/30.
 */
public class User implements Serializable{
    private long uid;
    private String uname;
    private byte age;
    private int  loginTime;
    private long ip;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public byte getAge() {
        return age;
    }

    public void setAge(byte age) {
        this.age = age;
    }

    public int getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(int loginTime) {
        this.loginTime = loginTime;
    }

    public long getIp() {
        return ip;
    }

    public void setIp(long ip) {
        this.ip = ip;
    }
}
