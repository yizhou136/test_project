package com.zy.nut.relayer.server;

import com.zy.nut.relayer.common.beans.RoomMsg;
import com.zy.nut.relayer.common.container.RelayerServerContainer;
import com.zy.nut.relayer.server.service.MsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.net.URL;

/**
 * Created by Administrator on 2016/11/6.
 */
//@Configuration
@SpringBootApplication
//@EnableJpaRepositories("com.zy.nut.relayer.common.beans.*")
//@ComponentScan(basePackages = { "com.zy.nut.relayer.common.beans.*" })
//@EntityScan("com.zy.nut.relayer.common.beans.*")
public class BootRelayerServer {
    private static  final Logger logger = LoggerFactory.getLogger(BootRelayerServer.class);

    public static void main(String args[]){
        //SpringApplication.run(BootRelayerServer.class, args);
        SpringApplication springApplication = new SpringApplication(BootRelayerServer.class);
        springApplication.addListeners(new ApplicationListener<ContextRefreshedEvent>() {
            @Override
            public void onApplicationEvent(ContextRefreshedEvent event) {
                logger.debug("ContextRefreshedEvent {}",event);
                MsgService msgService = (MsgService) event.getApplicationContext().getBean("msgService");
                RoomMsg roomMsg = new RoomMsg();
                roomMsg.setFuid(123);
                roomMsg.setMsg("hello");
                roomMsg.setCtime(System.currentTimeMillis());
                roomMsg.setLctime(System.currentTimeMillis());
                msgService.sendRoomMsg(roomMsg);
                logger.debug("sendRoomMsg end roomMsg:"+roomMsg);
            }
        });
        springApplication.run(args);
    }

    public static void main2(String argv[]){
        //SpringApplication.run(BootRelayerServer.class);
        String path = "relayer.properties";
        if (argv.length > 0)
            path = argv[0];
        System.out.println("read path:"+path);
        URL url = RelayerServerContainer.class.getClassLoader().getResource(path);
        RelayerServerContainer relayerContainer = null;
        try {
            relayerContainer = new RelayerServerContainer(url);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println(relayerContainer.getConfiguration());
    }
}
