package com.zy.nut.web.test.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.log4j.Logger;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by zhougb on 2017/2/27.
 */
public class TestHbase {
    private static Configuration cfg;
    private static HTablePool tablePool;
    private Logger log = Logger.getLogger(this.getClass().getName());

    static {
        cfg = HBaseConfiguration.create();

    /* 创建tablePool，并定义Pool大小 */
        tablePool = new HTablePool(cfg, 1000);
    }

    @Test
    public void testInsert() {
        try {
            final String TABLE_NAME = "test_table";

        /* 创建HTable对象获取表信息 */
        HTableInterface table = tablePool.getTable(TABLE_NAME);

        /* 创建Row Key */
        final String ROW_KEY = "JohnGao"
                    + ":"
                    + String.valueOf(Long.MAX_VALUE
                    - System.currentTimeMillis());

        /* 创建Put对象，插入行级数据 */
            Put put = new Put(ROW_KEY.getBytes());
            put.add("message".getBytes(), "content1".getBytes(),
                    "Hello HBase1".getBytes());
            put.add("message".getBytes(), "content2".getBytes(),
                    "Hello HBase2".getBytes());

        /* 开始执行数据添加 */
            table.put(put);

        /* 资源释放 */
            release(table);
            log.info("数据插入成功");
        } catch (IOException e) {
            log.error("数据插入失败", e);
        }
    }

    @Test
    public void testGet() {
        try {
            final String TABLE_NAME = "test_table";
        /* 创建HTable对象获取表信息 */
            HTableInterface table = tablePool.getTable(TABLE_NAME);

        /* 创建Row Key */
            final String ROW_KEY = "JohnGao"
                    + ":"
                    + String.valueOf(Long.MAX_VALUE
                    - System.currentTimeMillis());

            Get get = new Get(ROW_KEY.getBytes());

        /* 定义需要检索的列 */
            get.addColumn("message".getBytes(), "content1".getBytes());
            Result result = table.get(get);

        /* 输出数据 */
            System.out.println(Bytes.toString(result.getValue(
                    "message".getBytes(), "content1".getBytes())));

        /* 资源释放 */
            release(table);
        } catch (IOException e) {
            log.error("数据检索失败", e);
        }
    }

    /**
     * HBase资源释放
     *
     * @author JohnGao
     */
    public void release(HTableInterface table) throws IOException {
    /* 清空缓冲区并提交 */
        table.flushCommits();

    /* 将Table对象归还Pool */
        tablePool.putTable(table);
    }
}
