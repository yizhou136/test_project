package com.zy.nut.relayer.server.service.impl;

import com.zy.nut.common.msp.MspService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * Created by zhougb on 2017/2/24.
 */
@Service
public class MspServiceImpl implements MspService{
    private Logger logger = LoggerFactory.getLogger(MspServiceImpl.class);

    @Override
    public boolean sendTo(Serializable uid, byte[] data) {
        logger.info("sendTo uid:{} data:{}", uid, data);
        return false;
    }

    @Override
    public boolean publish(Serializable cid, byte[] data) {
        logger.info("publish cid:{} data:{}", cid, data);
        return false;
    }
}
