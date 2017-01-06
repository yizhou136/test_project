package com.zy.nut.common.kafka;

import org.apache.kafka.connect.data.Schema;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.source.SourceTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by zhougb on 2016/12/21.
 */
public class MyFileStreamSourceTask extends SourceTask {
    private static final Logger log = LoggerFactory.getLogger(MyFileStreamSourceTask.class);
    public static final String FILENAME_FIELD = "filename";
    public static final String POSITION_FIELD = "position";
    private static final Schema VALUE_SCHEMA;
    private String filename;
    private InputStream stream;
    private BufferedReader reader = null;
    private char[] buffer = new char[1024];
    private int offset = 0;
    private String topic = null;
    private Long streamOffset;

    public MyFileStreamSourceTask() {
    }

    public String version() {
        return (new MyFileStreamSourceConnector()).version();
    }

    public void start(Map<String, String> props) {
        this.filename = (String)props.get("file");
        if(this.filename == null || this.filename.isEmpty()) {
            this.stream = System.in;
            this.streamOffset = null;
            this.reader = new BufferedReader(new InputStreamReader(this.stream));
        }

        this.topic = (String)props.get("topic");
        if(this.topic == null) {
            throw new ConnectException("MyFileStreamSourceTask config missing topic setting");
        }

        log.debug("filename:{},  topic:{}", filename, topic);
    }

    public List<SourceRecord> poll() throws InterruptedException {
        if(this.stream == null) {
            try {
                this.stream = new FileInputStream(this.filename);
                Map e = this.context.offsetStorageReader().offset(Collections.singletonMap("filename", this.filename));
                if(e != null) {
                    Object records = e.get("position");
                    if(records != null && !(records instanceof Long)) {
                        throw new ConnectException("Offset position is the incorrect type");
                    }

                    if(records != null) {
                        log.debug("Found previous offset, trying to skip to file offset {}", records);
                        long nread = ((Long)records).longValue();

                        while(nread > 0L) {
                            try {
                                long e1 = this.stream.skip(nread);
                                nread -= e1;
                            } catch (IOException var13) {
                                log.error("Error while trying to seek to previous offset in file: ", var13);
                                throw new ConnectException(var13);
                            }
                        }

                        log.debug("Skipped to offset {}", records);
                    }

                    this.streamOffset = Long.valueOf(records != null?((Long)records).longValue():0L);
                } else {
                    this.streamOffset = Long.valueOf(0L);
                }

                this.reader = new BufferedReader(new InputStreamReader(this.stream));
                log.debug("Opened {} for reading", this.logFilename());
            } catch (FileNotFoundException var15) {
                log.warn("Couldn\'t find file {} for MyFileStreamSourceTask, sleeping to wait for it to be created", this.logFilename());
                synchronized(this) {
                    this.wait(1000L);
                    return null;
                }
            }
        }

        try {
            BufferedReader e2;
            synchronized(this) {
                e2 = this.reader;
            }

            if(e2 == null) {
                return null;
            } else {
                ArrayList records1 = null;
                int nread1 = 0;

                while(true) {
                    do {
                        log.debug("Read before start ready {}", logFilename());
                        if(!e2.ready()) {
                            log.debug("Read before cant ready {}  nread1:{}",
                                    logFilename(), nread1);
                            if(nread1 <= 0) {
                                synchronized(this) {
                                    this.wait(1000L);
                                }
                            }

                            return records1;
                        }

                        log.debug("Read before {}", logFilename());
                        nread1 = e2.read(this.buffer, this.offset, this.buffer.length - this.offset);
                        log.debug("Read {} bytes from {} nread1: {}",
                                Integer.valueOf(nread1),
                                this.logFilename(), nread1);
                    } while(nread1 <= 0);

                    this.offset += nread1;
                    if(this.offset == this.buffer.length) {
                        char[] line = new char[this.buffer.length * 2];
                        System.arraycopy(this.buffer, 0, line, 0, this.buffer.length);
                        this.buffer = line;
                    }

                    while(true) {
                        String line1 = this.extractLine();
                        if(line1 != null) {
                            log.debug("Read a line from {}", this.logFilename());
                            if(records1 == null) {
                                records1 = new ArrayList();
                            }

                            records1.add(new SourceRecord(this.offsetKey(this.filename), this.offsetValue(this.streamOffset), this.topic, VALUE_SCHEMA, line1));
                        }

                        if(line1 == null) {
                            break;
                        }
                    }
                }
            }
        } catch (IOException var14) {
            return null;
        }
    }

    private String extractLine() {
        int until = -1;
        int newStart = -1;

        for(int result = 0; result < this.offset; ++result) {
            if(this.buffer[result] == 10) {
                until = result;
                newStart = result + 1;
                break;
            }

            if(this.buffer[result] == 13) {
                if(result + 1 >= this.offset) {
                    return null;
                }

                until = result;
                newStart = this.buffer[result + 1] == 10?result + 2:result + 1;
                break;
            }
        }

        if(until != -1) {
            String var4 = new String(this.buffer, 0, until);
            System.arraycopy(this.buffer, newStart, this.buffer, 0, this.buffer.length - newStart);
            this.offset -= newStart;
            if(this.streamOffset != null) {
                this.streamOffset = Long.valueOf(this.streamOffset.longValue() + (long)newStart);
            }

            return var4;
        } else {
            return null;
        }
    }

    public void stop() {
        log.trace("Stopping");
        synchronized(this) {
            try {
                if(this.stream != null && this.stream != System.in) {
                    this.stream.close();
                    log.trace("Closed input stream");
                }
            } catch (IOException var4) {
                log.error("Failed to close MyFileStreamSourceTask stream: ", var4);
            }

            this.notify();
        }
    }

    private Map<String, String> offsetKey(String filename) {
        return Collections.singletonMap("filename", filename);
    }

    private Map<String, Long> offsetValue(Long pos) {
        return Collections.singletonMap("position", pos);
    }

    private String logFilename() {
        return this.filename == null?"stdin":this.filename;
    }

    static {
        VALUE_SCHEMA = Schema.STRING_SCHEMA;
    }
}
