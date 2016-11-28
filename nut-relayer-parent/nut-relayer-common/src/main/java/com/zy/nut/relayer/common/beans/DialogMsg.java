package com.zy.nut.relayer.common.beans;

/**
 * Created by Administrator on 2016/11/28.
 */
public class DialogMsg extends BaseMsg{
    private long fuid;
    private long tuid;

    public long getFuid() {
        return fuid;
    }

    public void setFuid(long fuid) {
        this.fuid = fuid;
    }

    public long getTuid() {
        return tuid;
    }

    public void setTuid(long tuid) {
        this.tuid = tuid;
    }
}
