package com.zy.nut.common.msp;

import java.io.Serializable;

/**
 * Created by zhougb on 2017/2/24.
 */
public interface MspService {
    boolean sendTo(Serializable uid, byte[] data);

    boolean publish(Serializable cid, byte[] data);
}