package com.zy.nut.relayer.common.container;

import com.zy.nut.relayer.common.remoting.exchange.TransformData;

/**
 * Created by Administrator on 2016/11/12.
 */
public class ContainerExchangeAdapter implements ContainerExchange{

    public void receiveFromServer(byte[] data) {

    }

    public void receiveFromBackend(byte[] data) {

    }

    public void sendToFrontEnd(TransformData transformData) {

    }

    public void sendToBackEnd(TransformData transformData) {

    }

    public void sendToLeadingServers(Object msg, boolean isFanout) {

    }

    public void start() {

    }

    public void stop() {

    }
}
