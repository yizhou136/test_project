package com.zy.nut.services.serviceImpl;

import com.zy.nut.common.msp.MsBackService;
import com.zy.nut.common.msp.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by zhougb on 2017/2/24.
 */
@Service
public class MsBackServiceImpl implements MsBackService{
    private Logger logger = LoggerFactory.getLogger(MsBackServiceImpl.class);

    public Response nofity(byte[] data) {
        logger.info("nofity data:{}", data);
        return new Response((byte) 0,"OK");
    }
}
