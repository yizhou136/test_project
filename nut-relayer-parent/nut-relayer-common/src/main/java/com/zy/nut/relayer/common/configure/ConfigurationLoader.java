package com.zy.nut.relayer.common.configure;

import com.zy.nut.relayer.common.utils.StringUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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

    public static final Configuration load(URL url){
        try {
            return load(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static final Configuration parseConfiguration(Properties properties){
        Configuration configuration = new Configuration();
        configuration.setServerAddress(properties.getProperty("server.address"));

        Set<String> projectsSet = new LinkedHashSet<String>();
        String projects = properties.getProperty("projects");
        if (!StringUtils.isEmpty(projects)){
            for (String project:projects.split(",")){
                project = project.trim();
                projectsSet.add(project);
            }
        }else {
            projectsSet.add("default");
        }
        configuration.setProjects(projectsSet);

        String clustersStr = properties.getProperty("clusters");
        if (!StringUtils.isEmpty(clustersStr)){
            Set<String> clustersSet = new LinkedHashSet<String>();
            Map<String,Cluster> clusterMap = new HashMap<String,Cluster>();
            for(String clusterName : clustersStr.split(",")){
                clusterName = clusterName.trim();
                String key = clusterName+".servers";
                String clusterServersStr = properties.getProperty(key);
                if (StringUtils.isEmpty(clusterServersStr))
                    continue;

                Cluster cluster = new Cluster();
                key = String.format("%s.max_transmitter_rate", clusterName);
                String val = properties.getProperty(key);
                if (StringUtils.isInteger(val))
                    cluster.setMaxTransmitterRate(Integer.parseInt(val));
                key = String.format("%s.min_transmitter_rate", clusterName);
                val = properties.getProperty(key);
                if (StringUtils.isInteger(val))
                    cluster.setMinTransmitterRate(Integer.parseInt(val));


                cluster.setName(clusterName);
                Set<String> serversSet = new LinkedHashSet<String>();
                for(String server:clusterServersStr.split(",")){
                    if (server.equals(configuration.getServerAddress())) {
                        configuration.setServerCluster(cluster);
                        continue;
                    }
                    serversSet.add(server);
                }
                cluster.setServers(serversSet);
                clusterMap.put(clusterName, cluster);
            }
            configuration.setClusters(clusterMap);
        }
        String clustersGroupsStr = properties.getProperty("clusters.groupes");
        if (!StringUtils.isEmpty(clustersGroupsStr)){
            Set<ClusterGroup> clusterGroupSet = new LinkedHashSet<ClusterGroup>();
            for(String cg: clustersGroupsStr.split(",")){
                cg = cg.trim();
                ClusterGroup clusterGroup = new ClusterGroup();
                clusterGroup.setGroupName(cg);
                String key = String.format("%s.max_transmitter_rate", cg);
                String val = properties.getProperty(key);
                if (StringUtils.isInteger(val))
                    clusterGroup.setMaxTransmitterRate(Integer.parseInt(val));
                key = String.format("%s.min_transmitter_rate", cg);
                val = properties.getProperty(key);
                if (StringUtils.isInteger(val))
                    clusterGroup.setMinTransmitterRate(Integer.parseInt(val));

                Set<Cluster> clusterSet = new LinkedHashSet<Cluster>();
                clusterGroup.setClusters(clusterSet);
                for (String cluserName : cg.split("-")) {
                    Cluster cluster = configuration.getClusterByName(cluserName);
                    if (cluster != null){
                        clusterSet.add(cluster);
                    }
                }

                clusterGroupSet.add(clusterGroup);
            }
            configuration.setClusterGroup(clusterGroupSet);
        }

        //parse amqp
        String amqpHost = properties.getProperty("amqp.host");
        if (!StringUtils.isEmpty(amqpHost)){
            String amqpPort = properties.getProperty("amqp.host");
            String amqpUsername = properties.getProperty("amqp.username");
            String amqpPassword = properties.getProperty("amqp.password");
            AMQPConf amqpConf = new AMQPConf();
            amqpConf.setHost(amqpHost);
            amqpConf.setPort(Integer.parseInt(amqpPort));
            amqpConf.setUsername(amqpUsername);
            amqpConf.setPassword(amqpPassword);
            configuration.setAmqpConf(amqpConf);
        }

        return configuration;
    }

    public static void main(String argv[]){
        Configuration configuration = load(Configuration.class.getClassLoader().getResourceAsStream("relayer.properties"));
        System.out.println(configuration);
    }
}
