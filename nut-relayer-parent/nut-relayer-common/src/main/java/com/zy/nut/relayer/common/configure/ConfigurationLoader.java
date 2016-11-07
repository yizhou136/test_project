package com.zy.nut.relayer.common.configure;

import com.zy.nut.relayer.common.utils.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by Administrator on 2016/11/7.
 */
public class ConfigurationLoader {

    public static final Configuration load(String propertiesFile){
        if (StringUtils.isEmpty(propertiesFile))
            return null;
        Configuration configuration = null;
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(propertiesFile));
            configuration = parseConfiguration(properties);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return configuration;
    }

    public static final Configuration load(InputStream inputStream){
        try {
            Properties properties = new Properties();
            properties.load(inputStream);
            return parseConfiguration(properties);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static final Configuration parseConfiguration(Properties properties){
        Configuration configuration = new Configuration();
        configuration.setServerAddress(properties.getProperty("server.address"));

        String clustersStr = properties.getProperty("clusters");
        if (!StringUtils.isEmpty(clustersStr)){
            Set<String> clustersSet = new LinkedHashSet<String>();
            Map<String,Set<String>> clusterServers = new HashMap<String, Set<String>>();
            for(String clusterStr : clustersStr.split(",")){
                String key = clusterStr+".servers";
                String clusterServersStr = properties.getProperty(key);
                if (StringUtils.isEmpty(clusterServersStr))
                    continue;
                Set<String> serversSet = new LinkedHashSet<String>();
                for(String server:clusterServersStr.split(",")){
                    serversSet.add(server);
                }
                clusterServers.put(key,serversSet);

                clustersSet.add(clusterStr);
            }
            configuration.setClusters(clustersSet);
            configuration.setClusterServers(clusterServers);
        }
        String clustersGroupsStr = properties.getProperty("clusters.groupes");
        if (!StringUtils.isEmpty(clustersGroupsStr)){
            Set<String> clustersGroupsSet = new LinkedHashSet<String>();
            for(String cg: clustersGroupsStr.split(",")){
                clustersGroupsSet.add(cg);
            }
            configuration.setClusterGroups(clustersGroupsSet);
        }

        return configuration;
    }

    public static void main(String argv[]){
        Configuration configuration = load(Configuration.class.getClassLoader().getResourceAsStream("relayer.properties"));
        System.out.println(configuration);
    }
}
