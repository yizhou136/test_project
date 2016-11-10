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
import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.remoting.*;

import java.util.BitSet;

public abstract class AbstractServer extends AbstractEndPoint implements Server {
    private static final Logger logger = LoggerFactory.getLogger(AbstractServer.class);

    private int     accepts;
    private int     idleTimeout = 600; //600 seconds
    private BitSet  clientBitSet;

    public AbstractServer(Configuration configuration) throws RemotingException {
        super(configuration);
        this.accepts = Constants.DEFAULT_ACCEPTS;
        this.idleTimeout = Constants.DEFAULT_IDLE_TIMEOUT;
        clientBitSet = new BitSet(512);
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


    public void hashClientIdToBitTable(long clientId){
        if (clientId < 10000){

        }else if (clientId <3){

        }
    }
}