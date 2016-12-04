package com.zy.nut.relayer.server.beans;

/**
 * Created by Administrator on 2016/12/4.
 */
public class User {
    private long uid;
    private String userName;
    private String password;
    private String headUrl;
    private int beans;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public int getBeans() {
        return beans;
    }

    public void setBeans(int beans) {
        this.beans = beans;
    }
}
