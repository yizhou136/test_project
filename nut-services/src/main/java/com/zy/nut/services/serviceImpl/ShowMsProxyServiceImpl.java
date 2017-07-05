package com.zy.nut.services.serviceImpl;

import com.alibaba.dubbo.rpc.RpcContext;
import com.zy.nut.common.beans.DialogMsg;
import com.zy.nut.common.beans.RoomMsg;
import com.zy.nut.common.msp.MsProxyService;
import com.zy.nut.common.msp.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @author by zy.
 */
@Service("showMspService")
public class ShowMsProxyServiceImpl implements MsProxyService{
    private Logger logger = LoggerFactory.getLogger(ShowMsProxyServiceImpl.class);

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Autowired
    @Qualifier("mspService")
    private MsProxyService msProxyService;

    public Set<String> route(String routeKey) {
        Set<String> nodeNames = redisTemplate.opsForSet().members(routeKey);
        logger.info("route routeKey:{} nodeNames:{}",
                routeKey, nodeNames);
        return nodeNames;
    }

    @Override
    public Response sendTo(DialogMsg dialogMsg) {
        String routeKey = String.format(MsBackServiceImpl.UserCHSTMP,
                dialogMsg.getTuid());
        Set<String> nodeNames = route(routeKey);
        logger.info("sendTo routeKey:{} nodeNames:{} dialogMsg:{}",
                routeKey, nodeNames, dialogMsg);
        for (String nodeName : nodeNames) {
            RpcContext.getContext().setAttachment("match", nodeName);
            msProxyService.sendTo(dialogMsg);
        }
        return Response.SUCCESSED_RES;
    }

    @Override
    public Response publish(RoomMsg roomMsg) {
        String routeKey = String.format(MsBackServiceImpl.RoomCHSTMP,
                roomMsg.getRid());
        Set<String> nodeNames = route(routeKey);
        logger.info("publish routeKey:{} nodeNames:{} roomMsg:{}",
                routeKey, nodeNames, roomMsg);
        for (String nodeName : nodeNames) {
            RpcContext.getContext().setAttachment("match", nodeName);
            msProxyService.publish(roomMsg);
        }
        return Response.SUCCESSED_RES;
    }
}
