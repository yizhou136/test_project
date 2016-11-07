package com.zy.nut.relayer.common.configure;

import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2016/11/7.
 */
public class Configuration {
    private String serverAddress;
    private Set<String> clusters;//c1_bj,c2_gz,c3_tj,c4_wh,c5_nj
    private Map<String,Set<String>> clusterServers;//192.168.5.209:8383,192.168.5.209:8484,192.168.5.209:8585
    private Set<String> clusterGroups;//c1_bj-c2_gz , c3_tj-c4_wh-c5_nj


    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public Set<String> getClusters() {
        return clusters;
    }

    public void setClusters(Set<String> clusters) {
        this.clusters = clusters;
    }

    public Map<String, Set<String>> getClusterServers() {
        return clusterServers;
    }

    public void setClusterServers(Map<String, Set<String>> clusterServers) {
        this.clusterServers = clusterServers;
    }

    public Set<String> getClusterGroups() {
        return clusterGroups;
    }

    public void setClusterGroups(Set<String> clusterGroups) {
        this.clusterGroups = clusterGroups;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("serverAddress:").append(getServerAddress())
        .append("\nclusters:").append(getClusters())
        .append("\nclusterServers:").append(getClusterServers())
        .append("\nclusterGroups:").append(getClusterGroups());
        return sb.toString();
    }
}