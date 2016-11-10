package com.zy.nut.relayer.common.remoting.exchange;

/**
 * Created by zhougb on 2016/11/10.
 */
public class RelayerRegistering {
    public static enum RelayerRegisteringType{
        DIRECTER_TYPE((byte) 0),
        FANOUT_TYPE((byte) 1);
        byte type;
        RelayerRegisteringType(byte t){
            this.type = t;
        }
        public byte getType() {
            return type;
        }
    }

    private long routingkey;
    private byte type;

    public long getRoutingkey() {
        return routingkey;
    }

    public void setRoutingkey(long routingkey) {
        this.routingkey = routingkey;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }
}