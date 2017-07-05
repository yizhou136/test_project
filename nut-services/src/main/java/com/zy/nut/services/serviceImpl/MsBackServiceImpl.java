package com.zy.nut.services.serviceImpl;

import com.zy.nut.common.beans.DialogMsg;
import com.zy.nut.common.beans.NodeServer;
import com.zy.nut.common.beans.RoomMsg;
import com.zy.nut.common.msp.MsBackService;
import com.zy.nut.common.msp.MsProxyService;
import com.zy.nut.common.msp.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Created by zhougb on 2017/2/24.
 */
@Service
public class MsBackServiceImpl implements MsBackService{
    private Logger logger = LoggerFactory.getLogger(MsBackServiceImpl.class);

    public static final String UserCHSTMP  = "u_chs_%d_s";
    public static final String RoomCHSTMP  = "r_chs_%d_s";

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    @Qualifier("showMspService")
    private MsProxyService msProxyService;


    public String route(String routeKey) {
        String nodeName = redisTemplate.opsForValue().get(routeKey);
        logger.info("route routeKey:{} nodeName:{}",
                routeKey, nodeName);
        return nodeName;
    }

    @Override
    public void nodeUp(NodeServer nodeServer) {

    }

    @Override
    public void nodeDown(String nodeName) {

    }

    @Override
    public void userLogin(long uid, String nodeName) {
        String key = String.format(UserCHSTMP, uid);
        logger.info("userLogin key:{} nodeName:{}", key, nodeName);
        redisTemplate.opsForSet().add(key, nodeName);
    }

    @Override
    public void userLogout(long uid, String nodeName) {
        String key = String.format(UserCHSTMP, uid);
        logger.info("userLogout key:{} nodeName:{}", key, nodeName);
        redisTemplate.opsForSet().remove(key, nodeName);
    }

    @Override
    public void userEnteredRoom(long uid, long rid, String nodeName) {
        String key = String.format(RoomCHSTMP, rid);
        logger.info("userEnteredRoom key:{} uid:{} nodeName:{}",
                key, uid, nodeName);
    }

    @Override
    public void userLeftRoom(long uid, long rid, String nodeName) {
        String key = String.format(RoomCHSTMP, rid);
        logger.info("userLeftRoom key:{} uid:{} nodeName:{}",
                key, uid, nodeName);
    }

    @Override
    public void acceptProxyHeartbeat(NodeServer nodeServer) {

    }

    @Override
    public Response notify(Object object) {
        logger.info("notify object:{}", object);
        if (object instanceof DialogMsg){
           return msProxyService.sendTo((DialogMsg) object);
        }else if (object instanceof RoomMsg){
           return msProxyService.publish((RoomMsg) object);
        }

        return new Response((byte) 0,"OK");
    }
}