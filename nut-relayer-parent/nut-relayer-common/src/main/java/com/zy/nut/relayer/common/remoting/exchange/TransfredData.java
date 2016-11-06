package com.zy.nut.relayer.common.remoting.exchange;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Administrator on 2016/11/6.
 */
public class TransfredData {
    private static final AtomicLong INVOKE_ID = new AtomicLong(0);

    private final long  mId;
    private boolean isEvent;
    private boolean isBroken;


    private String group;
    private String fid;
    private String tid;

    private Object data;

    public TransfredData(long id){
        this.mId = id;
    }
    public TransfredData(){
        mId = INVOKE_ID.getAndIncrement();
    }

    public long getId() {
        return mId;
    }

    public boolean isEvent() {
        return isEvent;
    }

    public void setEvent(boolean event) {
        isEvent = event;
    }

    public boolean isBroken() {
        return isBroken;
    }

    public void setBroken(boolean broken) {
        isBroken = broken;
    }

    public void setEvent(Object obj) {
        isEvent = true;
        this.data = obj;
    }

    public String getGroup() {
        return group;
    }
    public void setGroup(String group) {
        this.group = group;
    }

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
