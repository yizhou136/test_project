package com.zy.nut.relayer.common.configure;

import com.zy.nut.relayer.common.URL;
import com.zy.nut.relayer.common.utils.StringUtils;
import com.zy.nut.relayer.common.utils.UrlUtils;

import java.util.*;

/**
 * Created by Administrator on 2016/11/7.
 */
public class Configuration {
    private Set<String> projects;
    private String serverAddress;
    private Map<String,Cluster> clusters;
    private Set<ClusterGroup> clusterGroup;//c1_bj-c2_gz , c3_tj-c4_wh-c5_nj
    private AMQPConf amqpConf;

    private Cluster serverCluster;

    private String clusterServerAddress;

    public Configuration(){}

    public Configuration(Configuration configuration){
        this.serverAddress = configuration.getServerAddress();
        this.clusters = configuration.getClusters();
        this.clusterGroup = configuration.getClusterGroup();
        this.serverCluster = configuration.getServerCluster();
    }

    public Set<String> getProjects() {
        return projects;
    }

    public void setProjects(Set<String> projects) {
        this.projects = projects;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public Map<String, Cluster> getClusters() {
        return clusters;
    }

    public void setClusters(Map<String, Cluster> clusters) {
        this.clusters = clusters;
    }

    public Set<ClusterGroup> getClusterGroup() {
        return clusterGroup;
    }

    public void setClusterGroup(Set<ClusterGroup> clusterGroup) {
        this.clusterGroup = clusterGroup;
    }

    public Cluster getServerCluster() {
        return serverCluster;
    }

    public void setServerCluster(Cluster serverCluster) {
        this.serverCluster = serverCluster;
    }

    public boolean isServerClient() {
        return !StringUtils.isEmpty(clusterServerAddress);
    }

    public String getClusterServerAddress() {
        return clusterServerAddress;
    }

    public void setClusterServerAddress(String clusterServerAddress) {
        this.clusterServerAddress = clusterServerAddress;
    }

    public AMQPConf getAmqpConf() {
        return amqpConf;
    }

    public void setAmqpConf(AMQPConf amqpConf) {
        this.amqpConf = amqpConf;
    }

    public Cluster getClusterByName(String clusterName){
        Map<String,Cluster> map = getClusters();
        if (map != null && !map.isEmpty()){
            return map.get(clusterName);
        }
        return null;
    }

    public Set<String> getCurrentClusterServers(){
        Cluster cluster = getServerCluster();
        if (cluster != null){
            return cluster.getServers();
        }
        return Collections.emptySet();
    }

    public String getCurrentClusterName(){
        Cluster cluster = getServerCluster();
        if (cluster != null){
            return cluster.getName();
        }
        return "";
    }

    public boolean isClusterLeader(){
        Cluster cluster = getServerCluster();
        if (cluster == null) return false;
        int min = cluster.getMinTransmitterCount();

        Iterator<String> iterator = cluster.getServers().iterator();
        while (iterator.hasNext() && min > 0) {
            String sa = iterator.next();
            if (getServerAddress().equals(sa))
                return true;
            min--;
        }
        return false;
    }

    public URL getServerAddressParsedURL(){
        return UrlUtils.parseURL(getServerAddress(), null);
    }

    public URL getClusterServerAddressParsedURL(){
        return UrlUtils.parseURL(getClusterServerAddress(), null);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("serverAddress:").append(getServerAddress())
        .append("\nprojects:").append(getProjects())
        .append("\nclusters:").append(getClusters())
        .append("\nclusterGroup:").append(getClusterGroup())
        .append("\nserverCluster:").append(getServerCluster())
        .append("\namqpConf:").append(getAmqpConf());
        return sb.toString();
    }
}