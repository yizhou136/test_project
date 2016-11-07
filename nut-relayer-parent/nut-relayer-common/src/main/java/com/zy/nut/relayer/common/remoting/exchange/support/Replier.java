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
package com.zy.nut.relayer.common.remoting.exchange.support;

import com.zy.nut.relayer.common.remoting.RemotingException;
import com.zy.nut.relayer.common.remoting.exchange.ExchangeChannel;

/**
 * Replier. (API, Prototype, ThreadSafe)
 * 
 * @author william.liangf
 */
public interface Replier<T> {

    /**
     * reply.
     * 
     * @param channel
     * @param request
     * @return response
     * @throws RemotingException
     */
    Object reply(ExchangeChannel channel, T request) throws RemotingException;

}