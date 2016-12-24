package com.zy.nut.common.kafka;

import org.apache.kafka.connect.connector.Task;
import org.apache.kafka.connect.source.SourceTask;

import java.io.InputStream;
import java.util.Map;

/**
 * Created by zhougb on 2016/12/21.
 */
public class FileStreamSourceTask {//extends SourceTask{
    String filename;
    InputStream stream;
    String topic;

    //@Override
    public void start(Map<String, String> props) {
        //filename = props.get(FileStreamSourceConnector.FILE_CONFIG);
        //stream = openOrThrowError(filename);
        //topic = props.get(FileStreamSourceConnector.TOPIC_CONFIG);
    }

    //@Override
    public synchronized void stop() {
        //stream.close();
    }

    public String version() {
        return null;
    }
}
