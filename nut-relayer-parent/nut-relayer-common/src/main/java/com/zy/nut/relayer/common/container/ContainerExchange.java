package com.zy.nut.relayer.common.container;

import com.zy.nut.common.beans.exchange.TransformData;

/**
 * Created by zhougb on 2016/11/11.
 */
public interface ContainerExchange extends Container{

    void receiveFromServer(byte[] data);
    void receiveFromBackend(byte[] data);

    void sendToFrontEnd(TransformData transformData);
    void sendToBackEnd(TransformData transformData);

    void sendToLeadingServers(Object msg, boolean isFanout);
}
