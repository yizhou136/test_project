package com.zy.nut.common.kafka;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.common.utils.AppInfoParser;
import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhougb on 2016/12/21.
 */
public class MyFileStreamSourceConnector extends SourceConnector{
    private static final Logger log = LoggerFactory.getLogger(MyFileStreamSourceConnector.class);
    public static final String TOPIC_CONFIG = "topic";
    public static final String FILE_CONFIG = "file";
    private static final ConfigDef CONFIG_DEF;
    private String filename;
    private String topic;

    public MyFileStreamSourceConnector() {
    }

    public String version() {
        return AppInfoParser.getVersion();
    }

    public void start(Map<String, String> props) {
        log.info("start props:{}", props);
        this.filename = (String)props.get("file");
        this.topic = (String)props.get("topic");
        if(this.topic != null && !this.topic.isEmpty()) {
            if(this.topic.contains(",")) {
                throw new ConnectException("MyFileStreamSourceConnector should only have a single topic when used as a source.");
            }
        } else {
            throw new ConnectException("MyFileStreamSourceConnector configuration must include \'topic\' setting");
        }
    }

    public Class<? extends Task> taskClass() {
        return MyFileStreamSourceTask.class;
    }

    public List<Map<String, String>> taskConfigs(int maxTasks) {
        ArrayList configs = new ArrayList();
        HashMap config = new HashMap();
        if(this.filename != null) {
            config.put("file", this.filename);
        }

        config.put("topic", this.topic);
        configs.add(config);

        log.info("taskConfigs maxTasks:{}   configs:{}",
                maxTasks, configs);
        return configs;
    }

    public void stop() {
        log.info("stop");
    }

    public ConfigDef config() {
        log.info("config");
        return CONFIG_DEF;
    }

    static {
        CONFIG_DEF = (new ConfigDef()).define("file", ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "Source filename.").define("topic", ConfigDef.Type.STRING, ConfigDef.Importance.HIGH, "The topic to publish data to");
    }
}
