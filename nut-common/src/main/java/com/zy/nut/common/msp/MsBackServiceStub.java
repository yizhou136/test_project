package com.zy.nut.common.msp;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.rpc.RpcContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

/**
 * Created by Administrator on 2017/2/26.
 */
public class MsBackServiceStub implements MsBackService{
    private static Logger logger = LoggerFactory.getLogger(MsBackServiceStub.class);
    private MsBackService msBackService;

    public MsBackServiceStub(MsBackService msBackService){
        this.msBackService = msBackService;
    }


    @Override
    public Response nofity(byte[] data) {
        Random random = new Random();
        String group = random.nextBoolean() ?  "a" : "b";
        logger.info("stub notify data:{} group:{}", data, group);
        RpcContext.getContext().setAttachment(Constants.GROUP_KEY,group);
        return msBackService.nofity(data);
    }
}
