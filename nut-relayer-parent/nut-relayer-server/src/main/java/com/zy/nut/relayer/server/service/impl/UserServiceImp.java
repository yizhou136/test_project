package com.zy.nut.relayer.server.service.impl;

import com.zy.nut.relayer.common.beans.DialogMsg;
import com.zy.nut.relayer.common.beans.Response;
import com.zy.nut.relayer.common.beans.RoomMsg;
import com.zy.nut.relayer.common.remoting.RemotingException;
import com.zy.nut.relayer.common.remoting.exchange.RelayerEnterRoom;
import com.zy.nut.relayer.common.remoting.exchange.RelayerLeftRoom;
import com.zy.nut.relayer.common.remoting.exchange.RelayerLogin;
import com.zy.nut.relayer.common.remoting.exchange.RelayerLogout;
import com.zy.nut.relayer.common.remoting.exchange.header.RelayerCodecSupport;
import com.zy.nut.relayer.server.beans.UserChannel;
import com.zy.nut.relayer.server.dao.DialogMsgUao;
import com.zy.nut.relayer.server.dao.RoomMsgDao;
import com.zy.nut.relayer.server.service.SpringNettyContainer;
import com.zy.nut.relayer.server.service.UserService;
import io.netty.buffer.ByteBuf;
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

    @Autowired
    private RoomMsgDao roomMsgDao;
    @Autowired
    private DialogMsgUao dialogMsgUao;
    @Autowired
    private RelayerCodecSupport relayerCodecSupport;

    private Map<Long,UserChannel> userLoginedMap = new ConcurrentHashMap<Long,UserChannel>();
    private Map<Long,Set<UserChannel>> roomNettyesMap = new ConcurrentHashMap<Long,Set<UserChannel>>();
    private Integer syncIntArr[]  = new Integer[]{0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    private SpringNettyContainer springNettyContainer;

    @Override
    public void login(RelayerLogin login) {
        userLoginedMap.put(login.getUid(), UserChannel.getOrAddChannel(login.getChannel(), login.getUid()));
        login.setLoginedNetty(springNettyContainer.getNettyName());
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
        enterRoom.setLoginedNetty(springNettyContainer.getNettyName());
        Set<UserChannel> set = roomNettyesMap.get(enterRoom.getRid());
        if (set == null){
            Integer sync = genSync(enterRoom.getRid());
            synchronized (sync) {
                set = roomNettyesMap.get(enterRoom.getRid());
                if (set == null){
                    set = new HashSet<UserChannel>();
                    roomNettyesMap.put(enterRoom.getRid(), set);
                }
                set.add(UserChannel.getOrAddChannel(enterRoom.getChannel(), enterRoom.getUid()));
            }
        }else {
            synchronized (set){
                set.add(UserChannel.getOrAddChannel(enterRoom.getChannel(), enterRoom.getUid()));
            }
        }
        redisTemplate.opsForSet().add(genRoomNettyesRediskey(enterRoom.getRid()),
                enterRoom.getLoginedNetty());
    }

    @Override
    public void leftRoom(RelayerLeftRoom leftRoom) {
        Set<UserChannel> set = roomNettyesMap.get(leftRoom.getRid());
        if (set != null){
            synchronized (set){
                set.remove(UserChannel.getOrAddChannel(leftRoom.getChannel(), leftRoom.getUid()));
            }
        }
        redisTemplate.opsForSet().remove(genRoomNettyesRediskey(leftRoom.getRid()),leftRoom.getLoginedNetty());
    }

    @Override
    public void sendDialogMsg(DialogMsg dialogMsg) throws RemotingException{
        logger.debug("dialogMsg:{} roomMsgDao:{}", dialogMsg,roomMsgDao);
        UserChannel fUserChannel = userLoginedMap.get(dialogMsg.getFuid());
        UserChannel tUserChannel = userLoginedMap.get(dialogMsg.getTuid());

        long mid = dialogMsgUao.save(dialogMsg);
        if (mid < 0){
            if (fUserChannel != null)
                fUserChannel.send(Response.FAILURED_RES);
            return;
        }else {
            ByteBuf byteBuf = relayerCodecSupport.encodeToByteBuf(dialogMsg);
            if (fUserChannel != null) {
                byteBuf.retain();
                logger.debug("dialogMsg send to {}  byteBuf:{}", fUserChannel.getUid(), byteBuf.refCnt());
                fUserChannel.send(byteBuf);
            }
            if (tUserChannel != null) {
                //byteBuf.retain();
                logger.debug("dialogMsg send to {}  byteBuf:{}", tUserChannel.getUid(), byteBuf.refCnt());
                tUserChannel.send(byteBuf);
            }
            logger.debug("dialogMsg  before release  byteBuf:{}", byteBuf.refCnt());
        }
    }

    @Override
    public void sendRoomMsg(RoomMsg roomMsg) {
        logger.debug("roomMsg:{} roomMsgDao:{}", roomMsg,roomMsgDao);
        long mid = roomMsgDao.save(roomMsg);
        roomMsg.setMid(mid);
    }

    @Override
    public void sendGlobalMsg() {
        logger.debug("roomMsgDao:{}", roomMsgDao);
    }

    @Override
    public void dig(long rid) {

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