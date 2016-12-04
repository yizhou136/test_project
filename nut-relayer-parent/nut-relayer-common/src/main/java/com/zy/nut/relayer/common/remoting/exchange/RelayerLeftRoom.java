package com.zy.nut.relayer.common.remoting.exchange;

/**
 * Created by Administrator on 2016/12/3.
 */
public class RelayerLeftRoom {
    private long uid;
    private long rid;
    private String loginedNetty;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public String getLoginedNetty() {
        return loginedNetty;
    }

    public void setLoginedNetty(String loginedNetty) {
        this.loginedNetty = loginedNetty;
    }
}
