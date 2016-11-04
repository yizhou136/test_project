package com.zy.nut.common.beans;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/30.
 */
public class Order implements Serializable {
    private long oid;
    private long pid;
    private int ctime;
    private int account;

    public long getOid() {
        return oid;
    }

    public void setOid(long oid) {
        this.oid = oid;
    }

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public int getCtime() {
        return ctime;
    }

    public void setCtime(int ctime) {
        this.ctime = ctime;
    }

    public int getAccount() {
        return account;
    }

    public void setAccount(int account) {
        this.account = account;
    }
}
