package com.zy.nut.relayer.server.service.impl;

import com.zy.nut.common.beans.DialogMsg;
import com.zy.nut.common.beans.RoomMsg;
import com.zy.nut.common.msp.MsProxyService;
import com.zy.nut.common.msp.Response;
import com.zy.nut.relayer.common.remoting.Server;
import com.zy.nut.relayer.server.service.SpringNettyContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by zhougb on 2016/2/24.
 */
@Service
public class MspServiceImpl implements MsProxyService {
    private Logger logger = LoggerFactory.getLogger(MspServiceImpl.class);

    @Autowired
    private SpringNettyContainer springNettyContainer;

    private Server getServer(){
        return springNettyContainer.getServer();
    }

    @Override
    public Response sendTo(DialogMsg dialogMsg) {
        dialogMsg.setProxyReceiveBackMs(System.currentTimeMillis());
        getServer().sendTo(dialogMsg);
        return Response.SUCCESSED_RES;
    }

    @Override
    public Response publish(RoomMsg roomMsg) {
        getServer().publish(roomMsg);
        return Response.SUCCESSED_RES;
    }
}