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
package com.zy.nut.relayer.common;

import java.util.regex.Pattern;

public class Constants {

    public static final Pattern COMMA_SPLIT_PATTERN                = Pattern
                                                                    .compile("\\s*[,]+\\s*");

    public static final Pattern REGISTRY_SPLIT_PATTERN             = Pattern
                                                                    .compile("\\s*[|;]+\\s*");


    public static final int     DEFAULT_ACCEPTS                    = 0;
    public static final int     DEFAULT_IDLE_TIMEOUT               = 600 * 1000;

    public static final int     DEFAULT_PAYLOAD                    = 8 * 1024 * 1024;                      // 8M
    public static final int     DEFAULT_RECONNECT_PERIOD           = 2000;
    public static final int     DEFAULT_SHUTDOWN_TIMEOUT           = 1000 * 60 * 15;

    public static final int     DEFAULT_WARMUP                     = 10 * 60 * 1000;


    public static final int     DEFAULT_CONNECT_QUEUE_WARNING_SIZE = 1000;


    /**
     * 重试周期
     */
    public static final int DEFAULT_REGISTRY_RETRY_PERIOD          =  5 * 1000;
    

    public static final int     DEFAULT_REGISTRY_RECONNECT_PERIOD  = 3 * 1000;
    


    public static final int     DEFAULT_SESSION_TIMEOUT            = 60 * 1000;


    /**
     * 默认值毫秒，避免重新计算.
     */
    public static final int     DEFAULT_SERVER_SHUTDOWN_TIMEOUT    = 10000;

    public static final long DEFAULT_TPS_LIMIT_INTERVAL            = 60 * 1000;

    // default buffer size is 8k.
    public static final int     DEFAULT_BUFFER_SIZE                = 8 * 1024;


    public static final String  GROUP_KEY                          = "group";

    public static final String  PATH_KEY                           = "path";

    public static final String  INTERFACE_KEY                      = "interface";

    public static final String  GENERIC_KEY                        = "generic";

    public static final String  FILE_KEY                           = "file";

    public static final String  WAIT_KEY                           = "wait";

    public static final String  CLASSIFIER_KEY                     = "classifier";

    public static final String  VERSION_KEY                        = "version";

    public static final String  ANY_VALUE                          = "*";
    public static final String  COMMA_SEPARATOR                    = ",";

    public static final String  BACKUP_KEY                         = "backup";

    public static final String  ENABLED_KEY                        = "enabled";

    public static final String  CATEGORY_KEY                       = "category";

    public static final String  REMOVE_VALUE_PREFIX                = "-";

    public static final String  EMPTY_PROTOCOL                     = "empty";

    public static final String  PROVIDERS_CATEGORY                 = "providers";

    public static final String  DEFAULT_CATEGORY                   = PROVIDERS_CATEGORY;


    public static final String  DEFAULT_KEY_PREFIX                 = "default.";

    public static final String  ANYHOST_KEY                        = "anyhost";

    public static final String  ANYHOST_VALUE                      = "0.0.0.0";

    public static final String  LOCALHOST_KEY                      = "localhost";

    public static final String  BUFFER_KEY                         = "buffer";
}
