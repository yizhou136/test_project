package com.zy.nut.relayer.common.remoting.exchange;

/**
 * Created by zhougb on 2016/11/9.
 */
public class RelayerPingPong {
    //private String  serverId;
    private byte    clientBitTable[];
    private short   performance;


    public byte[] getClientBitTable() {
        return clientBitTable;
    }

    public void setClientBitTable(byte[] clientBitTable) {
        this.clientBitTable = clientBitTable;
    }

    public short getPerformance() {
        return performance;
    }

    public void setPerformance(short performance) {
        this.performance = performance;
    }
}
