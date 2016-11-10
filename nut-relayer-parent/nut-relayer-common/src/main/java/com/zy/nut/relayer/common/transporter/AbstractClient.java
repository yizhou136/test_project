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

import com.zy.nut.relayer.common.configure.Configuration;
import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.remoting.*;
import com.zy.nut.relayer.common.utils.NetUtils;

import java.net.InetSocketAddress;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractClient extends AbstractEndPoint implements Client {
    private static final Logger logger = LoggerFactory.getLogger(AbstractClient.class);
    private final boolean send_reconnect ;
    private final AtomicInteger reconnect_count = new AtomicInteger(0);
    private final Lock            connectLock = new ReentrantLock();

    public AbstractClient(Configuration configuration) {
        super(configuration);
        send_reconnect = true;
    }

    public void init() throws RemotingException {
        try {
            doOpen();
        } catch (Throwable t) {
            close();
            throw new RemotingException(getLocalAddress(), getRemoteAddress(),
                    "Failed to start " + getClass().getSimpleName() + " " + NetUtils.getLocalAddress()
                            + " connect to the server " + getRemoteAddress() + ", cause: " + t.getMessage(), t);
        }
        try {
            // connect.
            connect();
            if (logger.isInfoEnabled()) {
                logger.info("Start " + getClass().getSimpleName() + " " + NetUtils.getLocalAddress() + " connect to the server " + getRemoteAddress());
            }
        } catch (RemotingException t) {
            if (true) {
                close();
                throw t;
            } else {
                logger.warn("Failed to start " + getClass().getSimpleName() + " " + NetUtils.getLocalAddress()
                        + " connect to the server " + getRemoteAddress() + " (check == false, ignore and retry later!), cause: " + t.getMessage(), t);
            }
        } catch (Throwable t){
            close();
            throw new RemotingException(getLocalAddress(), getRemoteAddress(),
                    "Failed to start " + getClass().getSimpleName() + " " + NetUtils.getLocalAddress()
                            + " connect to the server " + getRemoteAddress() + ", cause: " + t.getMessage(), t);
        }
    }

    public InetSocketAddress getRemoteAddress() {
        Channel channel = getChannel();
        if (channel == null)
            return super.getRemoteAddress();
        return channel.getRemoteAddress();
    }

    public InetSocketAddress getLocalAddress() {
        Channel channel = getChannel();
        if (channel == null)
            return super.getLocalAddress();
        return channel.getLocalAddress();
    }

    public boolean isConnected() {
        Channel channel = getChannel();
        if (channel == null)
            return false;
        return channel.isConnected();
    }

    public Object getAttribute(String key) {
        Channel channel = getChannel();
        if (channel == null)
            return null;
        return channel.getAttribute(key);
    }

    public void setAttribute(String key, Object value) {
        Channel channel = getChannel();
        if (channel == null)
            return;
        channel.setAttribute(key, value);
    }

    public void removeAttribute(String key) {
        Channel channel = getChannel();
        if (channel == null)
            return;
        channel.removeAttribute(key);
    }

    public boolean hasAttribute(String key) {
        Channel channel = getChannel();
        if (channel == null)
            return false;
        return channel.hasAttribute(key);
    }
    
    public void send(Object message, boolean sent) throws RemotingException {
        if (send_reconnect && !isConnected()){
            connect();
        }
        Channel channel = getChannel();
        //TODO getChannel返回的状态是否包含null需要改进
        if (channel == null || ! channel.isConnected()) {
          throw new RemotingException(getChannel(), "message can not send, because channel is closed . url:" + getConfiguration().getServerAddress());
        }
        channel.send(message, sent);
    }
    
    protected void connect() throws RemotingException {
        connectLock.lock();
        try {
            if (isConnected()) {
                return;
            }
            //initConnectStatusCheckCommand();
            doConnect();
            if (! isConnected()) {
                RemotingException remotingException =  new RemotingException(getChannel(), "Failed connect to server " + getRemoteAddress() + " from " + getClass().getSimpleName() + " "
                                            + NetUtils.getLocalHost()
                                            + ", cause: Connect wait timeout: " + getTimeout() + "ms.");
            } else {
            	if (logger.isInfoEnabled()){
            		logger.info("Successed connect to server " + getRemoteAddress() + " from " + getClass().getSimpleName() + " "
                                            + NetUtils.getLocalHost()
                                            + ", channel is " + this.getChannel());
            	}
            }
            reconnect_count.set(0);
            //reconnect_error_log_flag.set(false);
        } catch (RemotingException e) {
            throw e;
        } catch (Throwable e) {
            throw new RemotingException(getChannel(), "Failed connect to server " + getRemoteAddress() + " from " + getClass().getSimpleName() + " "
                                        + NetUtils.getLocalHost()
                                        + ", cause: " + e.getMessage(), e);
        } finally {
            connectLock.unlock();
        }
    }

    public void disconnect() {
        connectLock.lock();
        try {
            //destroyConnectStatusCheckCommand();
            try {
                Channel channel = getChannel();
                if (channel != null) {
                    //channel.close();
                }
            } catch (Throwable e) {
                logger.warn(e.getMessage(), e);
            }
            try {
                doDisConnect();
            } catch (Throwable e) {
                logger.warn(e.getMessage(), e);
            }
        } finally {
            connectLock.unlock();
        }
    }
    
    public void reconnect() throws RemotingException {
        disconnect();
        connect();
    }

    public void close() {
    	try {
    		/*if (executor != null) {
    			ExecutorUtil.shutdownNow(executor, 100);
    		}*/
    	} catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }
        try {
            //super.close();
        } catch (Throwable e) {
            logger.warn(e.getMessage(), e);
        }
        try {
        	disconnect();
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
    
    @Override
    public String toString() {
        return getClass().getName() + " [" + getLocalAddress() + " -> " + getRemoteAddress() + "]";
    }

    /**
     * Open client.
     * 
     * @throws Throwable
     */
    protected abstract void doOpen() throws Throwable;

    /**
     * Close client.
     * 
     * @throws Throwable
     */
    protected abstract void doClose() throws Throwable;

    /**
     * Connect to server.
     * 
     * @throws Throwable
     */
    protected abstract void doConnect() throws Throwable;
    
    /**
     * disConnect to server.
     * 
     * @throws Throwable
     */
    protected abstract void doDisConnect() throws Throwable;

    /**
     * Get the connected channel.
     * 
     * @return channel
     */
    protected abstract Channel getChannel();

}
