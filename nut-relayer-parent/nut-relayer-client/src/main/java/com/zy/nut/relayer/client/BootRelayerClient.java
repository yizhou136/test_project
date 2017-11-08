package com.zy.nut.relayer.client;

import com.zy.nut.relayer.client.netty.MsProxyClient;
import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.transporter.netty.NettyClient;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zhougb on 2016/11/8.
 */
public class BootRelayerClient {
    private static final Logger logger = LoggerFactory.getLogger(BootRelayerClient.class);
    private static String[][] hostIpes = new String[][]{
            //new String[]{"0.0.0.0","8383"},
            //new String[]{"192.168.1.106","8484",
            new String[]{"0.0.0.0","8484"
            }
    };
    private static NioEventLoopGroup nioEventLoopGroup;
    private static List<MsProxyClient> msProxyClientList;
    private static int size = 2;

    public static void setUp(){
        nioEventLoopGroup = new NioEventLoopGroup(size);
        msProxyClientList = new ArrayList<>(size);
        Random random = new Random(System.currentTimeMillis());
        for (int i=0; i < size; i++){
            String[] hostip = hostIpes[random.nextInt(hostIpes.length)];
            MsProxyClient msProxyClient = new MsProxyClient(hostip[0], Integer.parseInt(hostip[1]),
                            Long.valueOf(i),nioEventLoopGroup);
            msProxyClientList.add(msProxyClient);
            logger.info("create msProxyClient:"+msProxyClientList.size());
        }
    }

    public static void login(){
        for (MsProxyClient msProxyClient : msProxyClientList){
            msProxyClient.login();
        }
    }

    public static void sendMsg(int cnt){
        Random random = new Random(System.currentTimeMillis());
        for (int i=0; i<cnt; i++){
            MsProxyClient msProxyClient = msProxyClientList.get(random.nextInt(msProxyClientList.size()));
            long tuid = random.nextInt(msProxyClientList.size());
            while (tuid == msProxyClient.getUid()){
                tuid = random.nextInt(msProxyClientList.size());
            }
            msProxyClient.sendDialogMsg(tuid);
            logger.info("sendDialogMsg from :"+msProxyClient.getUid()+" to:"+tuid);
        }
    }

    public static void main(String argv[]) throws InterruptedException {
        setUp();
        login();
        //sendMsg(3);
        //Thread.sleep(30000);
        //sendMsg(3);
        for (int i=0; i < 10; i++) {
            Thread.sleep(300);
            msProxyClientList.get(0).sendDialogMsg(1);
            System.out.println("send i"+i);
        }
    }
}
