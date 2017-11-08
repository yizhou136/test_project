package com.zy.nut.relayer.client;

import com.zy.nut.relayer.client.netty.MsProxyClient;
import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.utils.StringUtils;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by zhougb on 2016/11/8.
 */
public class UserClient {
    private static final Logger logger = LoggerFactory.getLogger(UserClient.class);
    private static NioEventLoopGroup nioEventLoopGroup;

    /*public static void sendMsg(int cnt){
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
    }*/

    public static void main(String argv[]) throws InterruptedException {
        //String[] hostip = hostIpes[random.nextInt(hostIpes.length)];
        Random random = new Random(System.currentTimeMillis());
        String host = "0.0.0.0";
        String port = "8484";
        nioEventLoopGroup = new NioEventLoopGroup(1);

        Long uid = Long.valueOf(1000);//(long)random.nextInt(1000);
        Long tuid = Long.valueOf(2000);

        if (argv.length == 1){
            uid = Long.valueOf(argv[0]);
        }else if (argv.length == 2){
            uid = Long.valueOf(argv[0]);
            tuid = Long.valueOf(argv[1]);
        }

        logger.info("uid:"+uid+" tuid:"+tuid);

        MsProxyClient msProxyClient = new MsProxyClient(host, Integer.parseInt(port),
                        uid,nioEventLoopGroup);
        msProxyClient.login();


        Scanner scanner = new Scanner( System.in);
        while (scanner.hasNext()){
            String line = scanner.nextLine();
            if (!StringUtils.isEmpty(line)) {
                //logger.info("read  line:" + line);
                msProxyClient.sendDialogMsg(tuid, line);
            }
        }
    }
}
