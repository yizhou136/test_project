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
package com.zy.nut.relayer.common.serialize;

import com.zy.nut.relayer.common.URL;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public interface Serialization {

    /**
     * get content type id
     * 
     * @return content type id
     */
    byte getContentTypeId();

    /**
     * get content type
     * 
     * @return content type
     */
    String getContentType();

    /**
     * create serializer
     * @param url 
     * @param output
     * @return serializer
     * @throws IOException
     */
    ObjectOutput serialize(URL url, OutputStream output) throws IOException;

    /**
     * create deserializer
     * @param url 
     * @param input
     * @return deserializer
     * @throws IOException
     */
    ObjectInput deserialize(URL url, InputStream input) throws IOException;

}