/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.zy.nut.relayer.common.transporter;


import com.zy.nut.relayer.common.Constants;
import com.zy.nut.relayer.common.configure.Configuration;
import com.zy.nut.relayer.common.container.ContainerExchange;
import com.zy.nut.relayer.common.remoting.*;
import com.zy.nut.relayer.common.remoting.exchange.DownStreamMap;
import com.zy.nut.common.beans.exchange.RelayerRegisteringUnRegistering;
import com.zy.nut.common.beans.exchange.TransformData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class AbstractServer extends AbstractEndPoint implements Server {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractServer.class);

    private int     accepts;
    private int     idleTimeout = 600; //600 seconds
    private String serverName;
    private DownStreamMap serverDownStreamMap;
    private DownStreamMap normalDownStreamMap;
    private ContainerExchange containerExchange;
    protected List<ChannelInitializerRegister> initializerRegisterList;

    public AbstractServer(Configuration configuration, ContainerExchange containerExchange, List<ChannelInitializerRegister> initializerRegisterList) throws RemotingException {
        super(configuration);
        this.accepts = Constants.DEFAULT_ACCEPTS;
        this.idleTimeout = Constants.DEFAULT_IDLE_TIMEOUT;
        this.containerExchange = containerExchange;
        if (configuration.isClusterLeader())
            serverDownStreamMap = new DownStreamMap(true);
        normalDownStreamMap = new DownStreamMap(false);
        this.initializerRegisterList = initializerRegisterList;
        if (configuration != null) {
            serverName = String.format("%s_%s",
                    configuration.getServerCluster().getName(),
                    configuration.getServerAddress());
        }
        try {
            doOpen();
            if (logger.isInfoEnabled()) {
                logger.info("Start " + getClass().getSimpleName() + " bind " + getBindAddress() + ", export " + getLocalAddress());
            }
        } catch (Throwable t) {
            throw new RemotingException(getLocalAddress(), getRemoteAddress(), "Failed to bind " + getClass().getSimpleName()
                                        + " on " + getLocalAddress() + ", cause: " + t.getMessage(), t);
        }
    }



    protected abstract void doOpen() throws Throwable;
    
    protected abstract void doClose() throws Throwable;


    /*public void send(Object message, boolean sent) throws RemotingException {
        Collection<Channel> channels = getChannels();
        for (Channel channel : channels) {
            if (channel.isConnected()) {
                channel.send(message, sent);
            }
        }
    }*/
    
    public void close() {
        if (logger.isInfoEnabled()) {
            logger.info("Close " + getClass().getSimpleName() + " bind " + getBindAddress() + ", export " + getLocalAddress());
        }
        //ExecutorUtil.shutdownNow(executor ,100);
        try {
            super.close();
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }
        try {
            doClose();
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }
    }
    
    public void close(int timeout) {
        //ExecutorUtil.gracefulShutdown(executor ,timeout);
        close();
    }


    /*public void connected(Channel ch) throws RemotingException {
        Collection<Channel> channels = getChannels();
        if (accepts > 0 && channels.size() > accepts) {
            logger.error("Close channel " + ch + ", cause: The server " + ch.getLocalAddress() + " connections greater than max config " + accepts);
            ch.close();
            return;
        }
        super.connected(ch);
    }
    
    @Override
    public void disconnected(Channel ch) throws RemotingException {
        Collection<Channel> channels = getChannels();
        if (channels.size() == 0){
            logger.warn("All clients has discontected from " + ch.getLocalAddress() + ". You can graceful shutdown now.");
        }
        super.disconnected(ch);
    }*/

    @Override
    public String getNodeName() {
       return serverName;
    }

    public void handleRegUnreg(Channel channel,
                               RelayerRegisteringUnRegistering relayerRegisteringUnRegistering) {
        /*if (relayerRegisteringUnRegistering.isServerClient()) {
            if (serverDownStreamMap == null && getConfiguration().isClusterLeader()){
                serverDownStreamMap = new DownStreamMap(true);
            }
            if (relayerRegisteringUnRegistering.isRegAction()){
                serverDownStreamMap.registerDownStreamClient(relayerRegisteringUnRegistering.getProject(),
                        relayerRegisteringUnRegistering.getMatchType(),
                        relayerRegisteringUnRegistering.getMatchConditiones(), channel);
            }else {
                serverDownStreamMap.unregisterDownStreamClient(relayerRegisteringUnRegistering.getProject(),
                        relayerRegisteringUnRegistering.getMatchType(),
                        relayerRegisteringUnRegistering.getMatchConditiones());
            }
        }else {
            if (relayerRegisteringUnRegistering.isRegAction()){
                normalDownStreamMap.registerDownStreamClient(relayerRegisteringUnRegistering.getProject(),
                        relayerRegisteringUnRegistering.getMatchType(),
                        relayerRegisteringUnRegistering.getMatchConditiones(), channel);
                relayerRegisteringUnRegistering.setRegisterType(
                        RelayerRegisteringUnRegistering.RelayerRegisteringType.SERVER_REG_CLIENT.getType());
            }else {
                normalDownStreamMap.unregisterDownStreamClient(relayerRegisteringUnRegistering.getProject(),
                        relayerRegisteringUnRegistering.getMatchType(),
                        relayerRegisteringUnRegistering.getMatchConditiones());
                relayerRegisteringUnRegistering.setRegisterType(
                        RelayerRegisteringUnRegistering.RelayerRegisteringType.SERVER_UNREG_CLIENT.getType());
            }
            //todo register  server leader;
            containerExchange.sendToLeadingServers(relayerRegisteringUnRegistering, true);
        }*/
    }

    public void sendToFrontEnd(TransformData transformData) {
        /*List<Channel> channelList = normalDownStreamMap.receiveChannelByRoutingKey(transformData.getProject(),
                transformData.getMatchType(), transformData.getMatchConditiones());
        List<Channel> serverClientChannelList = serverDownStreamMap != null ?
                serverDownStreamMap.receiveChannelByRoutingKey(transformData.getProject(),
                transformData.getMatchType(), transformData.getMatchConditiones()) : null;

        if (channelList == null){
            if (serverClientChannelList == null)
                return;
            channelList = serverClientChannelList;
        }else {
            if (serverClientChannelList != null)
                channelList.addAll(serverClientChannelList);
        }

        for (Channel channel : channelList) {
            try {
                channel.send(transformData, false);
            } catch (RemotingException e) {
                e.printStackTrace();
            }
        }*/
    }

    public void sendToBackEnd(TransformData transformData) {
        containerExchange.sendToBackEnd(transformData);
    }
}