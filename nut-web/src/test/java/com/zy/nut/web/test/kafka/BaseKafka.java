package com.zy.nut.web.test.kafka;

/**
 * Created by zhougb on 2016/12/22.
 */
public class BaseKafka {
    protected static final String TOPIC_NAME_PATTERN = "t_%d_%d_n";

    protected static final int REPLICATION_FACTORY = 1;
    protected static final int PARTITION = 2;

    protected static final String GLOBAL_TOPIC_NAME;
    static {
        GLOBAL_TOPIC_NAME = String.format(TOPIC_NAME_PATTERN, REPLICATION_FACTORY, PARTITION);
    }
}
