package com.zy.nut.relayer.server.service.impl;

import com.zy.nut.relayer.common.remoting.exchange.RelayerEnterRoom;
import com.zy.nut.relayer.common.remoting.exchange.RelayerLeftRoom;
import com.zy.nut.relayer.common.remoting.exchange.RelayerLogin;
import com.zy.nut.relayer.common.remoting.exchange.RelayerLogout;
import com.zy.nut.relayer.server.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2016/12/3.
 */
@Service
public class UserServiceImp implements UserService{
    private static  final Logger logger = LoggerFactory.getLogger(MsgServerImp.class);

    private Map<Long,RelayerLogin> userLoginedMap = new ConcurrentHashMap<Long,RelayerLogin>();
    private Map<Long,Set<RelayerEnterRoom>> roomNettyesMap = new ConcurrentHashMap<Long,Set<RelayerEnterRoom>>();
    private Integer syncIntArr[]  = new Integer[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public void login(RelayerLogin login) {
        userLoginedMap.put(login.getUid(), login);
        redisTemplate.opsForHash()
                .put(genUserEntityRediskey(login.getUid()),"logined_netty", login.getLoginedNetty());
    }

    @Override
    public void logout(RelayerLogout logout) {
        userLoginedMap.remove(logout.getUid());
        redisTemplate.opsForHash().delete(genUserEntityRediskey(logout.getUid()),"logined_netty");
    }

    @Override
    public void enterRoom(RelayerEnterRoom enterRoom) {
        Set<RelayerEnterRoom> set = roomNettyesMap.get(enterRoom.getRid());
        if (set == null){
            Integer sync = genSync(enterRoom.getRid());
            synchronized (sync) {
                set = roomNettyesMap.get(enterRoom.getRid());
                if (set == null){
                    set = new HashSet<RelayerEnterRoom>();
                    roomNettyesMap.put(enterRoom.getRid(), set);
                }
                set.add(enterRoom);
            }
        }else {
            synchronized (set){
                set.add(enterRoom);
            }
        }
        redisTemplate.opsForSet().add(genRoomNettyesRediskey(enterRoom.getRid()),
                enterRoom.getLoginedNetty());
    }

    @Override
    public void leftRoom(RelayerLeftRoom leftRoom) {
        Set<RelayerEnterRoom> set = roomNettyesMap.get(leftRoom.getRid());
        if (set != null){
            synchronized (set){
                set.remove(leftRoom.getLoginedNetty());
            }
        }
        redisTemplate.opsForSet().remove(genRoomNettyesRediskey(leftRoom.getRid()),leftRoom.getLoginedNetty());
    }


    private String genUserEntityRediskey(long uid){
        return String.format("user_%d_h",uid);
    }

    private String genRoomNettyesRediskey(long rid){
        return String.format("room_nettyes_%d_s", rid);
    }

    private Integer genSync(long id){
        return syncIntArr[(int)id%syncIntArr.length];
    }
}