package com.zy.nut.common.beans;

//import javax.persistence.Entity;

/**
 * Created by Administrator on 2016/11/28.
 */
//@Entity
public class RoomMsg extends BaseMsg{
    private long fuid;
    private long rid;

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public long getFuid() {
        return fuid;
    }

    public void setFuid(long fuid) {
        this.fuid = fuid;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(super.toString());
        return stringBuilder.append("fuid:").append(getFuid()).toString();
    }
}
