package com.zy.nut.services.controller;

import com.zy.nut.common.beans.NodeServer;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author by zy.
 */
@RestController
public class MsProxyNodeController {

    @RequestMapping("/nodes")
    public List<NodeServer> nodeServers(){

        return null;
    }
}