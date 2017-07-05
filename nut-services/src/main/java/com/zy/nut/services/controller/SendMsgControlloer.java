package com.zy.nut.services.controller;

import com.zy.nut.common.beans.DialogMsg;
import com.zy.nut.common.msp.MsProxyService;
import com.zy.nut.common.msp.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by zy.
 */
@RestController
public class SendMsgControlloer {
    private final static Logger logger = LoggerFactory.getLogger(SendMsgControlloer.class);

    @Autowired
    @Qualifier("showMspService")
    private MsProxyService showMsProxyService;

    @RequestMapping("sendto")
    public Response sendTo(Long fuid, Long tuid, String msg){
        logger.info("sendTo fuid:{} uid:{} msg:{}",
                fuid, tuid, msg);
        DialogMsg dialogMsg = new DialogMsg();
        dialogMsg.setFuid(fuid);
        dialogMsg.setTuid(tuid);
        dialogMsg.setMsg(msg);
        return showMsProxyService.sendTo(dialogMsg);
    }
}