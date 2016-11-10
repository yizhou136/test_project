package com.zy.nut.relayer.common.configure;

import java.util.Set;

/**
 * Created by zhougb on 2016/11/8.
 */
public class Cluster {
    private String name;
    private Set<String> servers;
    private int maxTransmitterRate;
    private int minTransmitterRate;

    private Set<ClusterGroup> groups;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getServers() {
        return servers;
    }

    public void setServers(Set<String> servers) {
        this.servers = servers;
    }

    public int getMaxTransmitterRate() {
        return maxTransmitterRate;
    }

    public void setMaxTransmitterRate(int maxTransmitterRate) {
        this.maxTransmitterRate = maxTransmitterRate;
    }

    public int getMinTransmitterRate() {
        return minTransmitterRate;
    }

    public void setMinTransmitterRate(int minTransmitterRate) {
        this.minTransmitterRate = minTransmitterRate;
    }

    public int getConfigedServerSize(){
        if (servers == null || servers.isEmpty())
            return  0;
        return servers.size();
    }

    public int getMinTransmitterCount(){
        double rate = getMinTransmitterRate() / 100;
        int serverSize = getConfigedServerSize();
        int res = (int)Math.ceil(rate * serverSize);
        res = Math.max(res, 1);
        return  res;
    }

    public int getMaxTransmitterCount(){
        double rate = getMaxTransmitterRate() / 100;
        int serverSize = getConfigedServerSize();
        int res = (int)Math.floor(rate * serverSize);
        res = Math.max(res, 1);
        return  res;
    }

    /*public int getAppropriateTransmitterCount(){
        int min = getMinTransmitterCount();
        int max = getMaxTransmitterCount();
        //return Math.
    }*/

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{name:").append(getName())
        .append(", servers:").append(getServers())
        .append(", maxTransmitterRate:").append(getMaxTransmitterRate())
        .append(", minTransmitterRate:").append(getMinTransmitterRate())
        .append("}");
        return sb.toString();
    }
}
