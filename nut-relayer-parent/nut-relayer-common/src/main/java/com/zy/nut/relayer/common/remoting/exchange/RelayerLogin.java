package com.zy.nut.relayer.common.remoting.exchange;

/**
 * Created by zhougb on 2016/11/9.
 */
public class RelayerLogin {
    private long uid;
    private byte pid;
    private String loginedNetty;
    private String userName;
    private String password;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public byte getPid() {
        return pid;
    }

    public void setPid(byte pid) {
        this.pid = pid;
    }

    public String getLoginedNetty() {
        return loginedNetty;
    }

    public void setLoginedNetty(String loginedNetty) {
        this.loginedNetty = loginedNetty;
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
}
