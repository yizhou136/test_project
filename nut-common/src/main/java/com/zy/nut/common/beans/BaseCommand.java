package com.zy.nut.common.beans;

/**
 * @author zhouguobao
 * 2017/10/31
 */
public class BaseCommand {
    private int cmdType;
    private String data;

    public int getCmdType() {
        return cmdType;
    }

    public void setCmdType(int cmdType) {
        this.cmdType = cmdType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
