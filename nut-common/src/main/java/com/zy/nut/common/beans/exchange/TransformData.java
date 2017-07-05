package com.zy.nut.common.beans.exchange;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Administrator on 2016/11/6.
 */
public class TransformData {
    public static final String DefaultProject = "ALL";
    public static enum TRANSFORM_DATA_TYPE{
        DIRECT((byte)0),
        FANOUT((byte)1),
        TOPIC((byte)2);

        byte type;
        TRANSFORM_DATA_TYPE(byte t){
            type = t;
        }
        public byte getType() {
            return type;
        }
    }
    private transient byte   exchangeType;
    private transient String routingKey;

    private byte project;
    private byte   matchType;
    private String matchConditiones;//uid liveid  or more  split by ,

    private Object data;



    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public String getMatchConditiones() {
        return matchConditiones;
    }

    public void setMatchConditiones(String matchConditiones) {
        this.matchConditiones = matchConditiones;
    }

    public byte getProject() {
        return project;
    }

    public void setProject(byte project) {
        this.project = project;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public byte getExchangeType() {
        return exchangeType;
    }

    public void setExchangeType(byte exchangeType) {
        this.exchangeType = exchangeType;
    }

    public byte getMatchType() {
        return matchType;
    }

    public void setMatchType(byte matchType) {
        this.matchType = matchType;
    }

    public Set<String> convertMatchConditionesToSet(){
        if (Objects.isNull(matchConditiones) || "".equals(matchConditiones))
            return Collections.emptySet();
        Set<String> stringSet = new LinkedHashSet<String>();
        for(String p:matchConditiones.split(",")){
            stringSet.add(p);
        }
        return stringSet;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(" project:").append(getProject())
        .append(" matchType:").append(getMatchType())
        .append(" matchConditiones:").append(getMatchConditiones())
        .append(" exchangeType:").append(getExchangeType())
        .append(" routingkey:").append(getRoutingKey());
        return sb.toString();
    }
}
