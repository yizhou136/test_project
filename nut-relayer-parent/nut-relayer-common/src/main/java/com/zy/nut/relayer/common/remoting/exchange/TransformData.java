package com.zy.nut.relayer.common.remoting.exchange;

import com.zy.nut.relayer.common.utils.StringUtils;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Administrator on 2016/11/6.
 */
public class TransformData {
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
    private String project;
    private byte type;
    private String matchConditiones;//uid liveid  or more  split by ,

    private Object data;

    public String getProject() {
        return project;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public String getMatchConditiones() {
        return matchConditiones;
    }

    public void setMatchConditiones(String matchConditiones) {
        this.matchConditiones = matchConditiones;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Set<String> parseProject(){
        if (StringUtils.isEmpty(project) || !StringUtils.isContains(project, ","))
            return Collections.emptySet();
        Set<String> stringSet = new LinkedHashSet<String>();
        for(String p:project.split(",")){
            stringSet.add(p);
        }
        return stringSet;
    }
}
