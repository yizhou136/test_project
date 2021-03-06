package com.zy.nut.relayer.server.beans;

/**
 * Created by Administrator on 2016/2/4.
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


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User){
            if (getUid() == (((User) obj).getUid())){
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        int key = (int)getUid();
            key += ~(key << 15);
            key ^= (key >>> 10);
            key += (key << 3);
            key ^= (key >>> 6);
            key += ~(key << 11);
            key ^= (key >>> 16);
        return key;
    }
}