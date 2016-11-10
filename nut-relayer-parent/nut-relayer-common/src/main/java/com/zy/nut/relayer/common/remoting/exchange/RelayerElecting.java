package com.zy.nut.relayer.common.remoting.exchange;

/**
 * Created by zhougb on 2016/11/9.
 */
public class RelayerElecting {
    private String serverName;
    private int guessedConnections;
    private short   performance;


    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getGuessedConnections() {
        return guessedConnections;
    }

    public void setGuessedConnections(int guessedConnections) {
        this.guessedConnections = guessedConnections;
    }

    public short getPerformance() {
        return performance;
    }

    public void setPerformance(short performance) {
        this.performance = performance;
    }
}
