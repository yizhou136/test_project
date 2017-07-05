package com.zy.nut.common.msp;

import com.zy.nut.common.beans.NodeServer;

/**
 * Created by zhougb on 2017/2/24.
 */
public interface MsBackService {

    void nodeUp(NodeServer nodeServer);

    void nodeDown(String nodeName);

    void userLogin(long uid, String nodeName);

    void userLogout(long uid, String nodeName);

    void userEnteredRoom(long uid, long rid, String nodeName);

    void userLeftRoom(long uid, long rid, String nodeName);

    void acceptProxyHeartbeat(NodeServer nodeServer);

    Response notify(Object object);
}