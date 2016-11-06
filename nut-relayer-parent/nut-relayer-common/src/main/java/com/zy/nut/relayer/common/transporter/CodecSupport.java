/*
 * Copyright 1999-2011 Alibaba Group.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.zy.nut.relayer.common.transporter;


import com.zy.nut.relayer.common.Constants;
import com.zy.nut.relayer.common.URL;
import com.zy.nut.relayer.common.logger.Logger;
import com.zy.nut.relayer.common.logger.LoggerFactory;
import com.zy.nut.relayer.common.serialize.Serialization;
import com.zy.nut.relayer.common.serialize.support.dubbo.DubboSerialization;
import com.zy.nut.relayer.common.serialize.support.hessian.Hessian2Serialization;
import com.zy.nut.relayer.common.serialize.support.java.JavaSerialization;
import com.zy.nut.relayer.common.serialize.support.json.JsonSerialization;
import com.zy.nut.relayer.common.serialize.support.nativejava.NativeJavaSerialization;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author <a href="mailto:gang.lvg@alibaba-inc.com">kimi</a>
 */
public class CodecSupport {

    private static final Logger logger = LoggerFactory.getLogger(CodecSupport.class);

    private CodecSupport() {
    }

    private static Map<Byte, Serialization> ID_SERIALIZATION_MAP = new HashMap<Byte, Serialization>();

    static {
        Serialization serialization = new DubboSerialization();
        byte idByte = serialization.getContentTypeId();
        ID_SERIALIZATION_MAP.put(idByte, serialization);

        serialization = new NativeJavaSerialization();
        idByte = serialization.getContentTypeId();
        ID_SERIALIZATION_MAP.put(idByte, serialization);

        serialization = new JavaSerialization();
        idByte = serialization.getContentTypeId();
        ID_SERIALIZATION_MAP.put(idByte, serialization);

        serialization = new Hessian2Serialization();
        idByte = serialization.getContentTypeId();
        ID_SERIALIZATION_MAP.put(idByte, serialization);

        serialization = new JsonSerialization();
        idByte = serialization.getContentTypeId();
        ID_SERIALIZATION_MAP.put(idByte, serialization);
    }

    public static Serialization getSerializationById(Byte id) {
        return ID_SERIALIZATION_MAP.get(id);
    }

    public static Serialization getDefaultSerialization() {
        return ID_SERIALIZATION_MAP.get((byte)2);
    }

    public static Serialization getSerialization(URL url) {
        return null;
        /*return ExtensionLoader.getExtensionLoader(Serialization.class).getExtension(
            url.getParameter(Constants.SERIALIZATION_KEY, Constants.DEFAULT_REMOTING_SERIALIZATION));*/
    }

    public static Serialization getSerialization(URL url, Byte id) {
        Serialization result = getSerializationById(id);
        if (result == null) {
            result = getSerialization(url);
        }
        return result;
    }

}
