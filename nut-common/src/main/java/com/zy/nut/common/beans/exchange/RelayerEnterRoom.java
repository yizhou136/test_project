package com.zy.nut.common.beans.exchange;

/**
 * Created by Administrator on 2016/12/3.
 */
public class RelayerEnterRoom extends BaseRelayerBean{
    private long rid;
    private String loginedNetty;

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
