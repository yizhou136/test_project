package com.zy.nut.relayer.server.test;

import com.alibaba.dubbo.common.utils.ClassHelper;
import com.zy.nut.relayer.common.configure.Configuration;
import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.transporter.netty.NettyServer;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by Administrator on 2016/11/6.
 */
public class TestRelayerServer {
    private static final Logger logger = LoggerFactory.getLogger(TestRelayerServer.class);

    public static void main(String argv[]){
        try {
            //URL url = UrlUtils.parseURL("127.0.0.1:8282",null);
            //Configuration configuration = new Configuration();
            //NettyServer nettyServer = new NettyServer(configuration);

            String fileName = "classpath:dubbo/dubbo.properties";
            try {
                Enumeration e = ClassHelper.getClassLoader().getResources(fileName);
                List list = new ArrayList();

                while(e.hasMoreElements()) {
                    list.add(e.nextElement());
                }
                System.out.println(list);
            } catch (Throwable var33) {
                logger.warn("Fail to load " + fileName + " file: " + var33.getMessage(), var33);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
