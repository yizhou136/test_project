package com.zy.nut.relayer.server.service.impl;

import com.zy.nut.relayer.common.beans.DialogMsg;
import com.zy.nut.relayer.common.beans.RoomMsg;
import com.zy.nut.relayer.server.dao.RoomMsgDao;
import com.zy.nut.relayer.server.service.MsgService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/12/3.
 */
//@Service("msgService")
public class MsgServerImp implements MsgService{
    private static  final Logger logger = LoggerFactory.getLogger(MsgServerImp.class);

    @Autowired
    private RoomMsgDao roomMsgDao;

    @Override
    public void sendDialogMsg(DialogMsg dialogMsg) {
        logger.debug("dialogMsg:{} roomMsgDao:{}", dialogMsg,roomMsgDao);
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
}
