package com.zy.nut.relayer.common.configure;

import java.util.Set;

/**
 * Created by zhougb on 2016/11/8.
 */
public class ClusterGroup {
    private String groupName;
    private int maxTransmitterRate;
    private int minTransmitterRate;
    private Set<Cluster> clusters;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
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

    public Set<Cluster> getClusters() {
        return clusters;
    }

    public void setClusters(Set<Cluster> clusters) {
        this.clusters = clusters;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("groupName:").append(getGroupName())
        .append(" maxTransmitterRate:").append(getMaxTransmitterRate())
        .append(" minTransmitterRate:").append(getMinTransmitterRate())
        .append("\nclusters:{").append(getClusters())
        .append("}");
        return sb.toString();
    }
}
