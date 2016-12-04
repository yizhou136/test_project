package com.zy.nut.relayer.common.beans;

/**
 * Created by Administrator on 2016/11/28.
 */
public class BaseMsg {
    /*@Id
    @GeneratedValue*/
    private long mid;
    private String msg;
    private long ctime;
    private long lctime;

    public long getMid() {
        return mid;
    }

    public void setMid(long mid) {
        this.mid = mid;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }

    public long getLctime() {
        return lctime;
    }

    public void setLctime(long lctime) {
        this.lctime = lctime;
    }


    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("mid:").append(getMid())
                .append("msg:").append(getMsg())
                .append("ctime:").append(getCtime())
                .append("lctime:").append(getLctime());
        return stringBuilder.toString();
    }
}
