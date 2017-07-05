package com.zy.nut.common.beans;

//import javax.persistence.Entity;

/**
 * Created by Administrator on 2016/11/28.
 */
//@Entity
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
