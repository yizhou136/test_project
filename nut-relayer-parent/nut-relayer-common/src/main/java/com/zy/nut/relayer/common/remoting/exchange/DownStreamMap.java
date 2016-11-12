package com.zy.nut.relayer.common.remoting.exchange;

import com.zy.nut.relayer.common.remoting.Channel;
import com.zy.nut.relayer.common.transporter.netty.NettyChannel;
import com.zy.nut.relayer.common.utils.StringUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhougb on 2016/11/11.
 */
public class DownStreamMap {
    private Map<String,Map<String,DownStreamClient>> downStreamClients;
    private Map<String,Map<String,Object>> normalClients;
    private boolean isClusterLeader;

    public DownStreamMap(boolean isClusterLeader){
        downStreamClients = new ConcurrentHashMap<String, Map<String,DownStreamClient>>();
        normalClients = new ConcurrentHashMap<String, Map<String,Object>>();
        this.isClusterLeader = isClusterLeader;
    }

    public void unregisterDownStreamClient(String project, byte type,
                                         String matchConditiones){

    }

    public void registerDownStreamClient(String project, byte type,
                                         String matchConditiones,
                                         Channel channel){
        if (isClusterLeader) {
            Map<String, DownStreamClient> map = downStreamClients.get(project);
            if (map == null) {
                map = new ConcurrentHashMap<String, DownStreamClient>();
                downStreamClients.put(project, map);
            }
            String channelId = channel.getChannelId();
            DownStreamClient downStreamClient = map.get(channelId);
            if (downStreamClient == null) {
                downStreamClient = new DownStreamClient(channel);
                map.put(channelId, downStreamClient);
            }
            downStreamClient.register(type, matchConditiones);
        }else {
            Map<String,Object>  map = normalClients.get(project);
            if (map == null){
                map = new ConcurrentHashMap<String, Object>();
                normalClients.put(project, map);
            }

            Object object = map.get(matchConditiones);
            if (object == null){
                if (type == TransformData.TRANSFORM_DATA_TYPE.DIRECT.getType())
                    object = channel;
                else {
                    List<Channel> list = new LinkedList<Channel>();
                    list.add(channel);
                    object = list;
                }
                map.put(matchConditiones, object);
            }else {
                if (type == TransformData.TRANSFORM_DATA_TYPE.DIRECT.getType()){
                    map.put(matchConditiones, channel);
                }else {
                    List<Channel> list = (List)object;
                    list.add(channel);
                }
            }
        }
    }

    public List<Channel> receiveChannelByRoutingKey(String project, byte type,
                                                    String matchConditiones){
        if (StringUtils.isEmpty(matchConditiones))
            return null;
        List<Channel> list = null;
        if (isClusterLeader) {
            Map<String, DownStreamClient> map = downStreamClients.get(project);
            if (map == null)
                return null;

            String[] matches = matchConditiones.split(",");
            for (DownStreamClient downStreamClient : map.values()) {
                for (String match : matches) {
                    if (downStreamClient.hasContains(type, match)) {
                        if (list == null)
                            list = new LinkedList<Channel>();
                        list.add(downStreamClient.getChannel());
                    }
                }
            }
        }else {
            Map<String,Object> map = normalClients.get(project);
            if (map == null)
                return null;

            String[] matches = matchConditiones.split(",");
            if (type == TransformData.TRANSFORM_DATA_TYPE.DIRECT.getType()){
                for (String match : matches) {
                    Channel channel = (Channel) map.get(match);
                    if (channel != null){
                        if (list == null)
                            list = new LinkedList<Channel>();
                        list.add(channel);
                    }
                }
            }else {
                for (String match : matches) {
                    List<Channel> channelList = (List<Channel>) map.get(match);
                    if (channelList != null){
                        if (list == null)
                            list = new LinkedList<Channel>();
                        list.addAll(channelList);
                    }
                }
            }
        }

        return list;
    }
}
