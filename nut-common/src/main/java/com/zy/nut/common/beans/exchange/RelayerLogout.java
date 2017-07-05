package com.zy.nut.common.beans.exchange;

/**
 * Created by zhougb on 2016/11/9.
 */
public class RelayerLogout extends BaseRelayerBean{
    private long uid;
    private byte pid;
    private String loginedNetty;

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
}
