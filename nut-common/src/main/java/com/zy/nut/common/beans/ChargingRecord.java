package com.zy.nut.common.beans;

/**
 * Created by Administrator on 2016/11/28.
 */
public class ChargingRecord {
    private long uid;
    private double account;
    private long ctime;
    private String bank;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public double getAccount() {
        return account;
    }

    public void setAccount(double account) {
        this.account = account;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }
}
