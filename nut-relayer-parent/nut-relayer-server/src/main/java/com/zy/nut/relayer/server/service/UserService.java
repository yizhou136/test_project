package com.zy.nut.relayer.server.service;

import com.zy.nut.common.beans.DialogMsg;
import com.zy.nut.common.beans.RoomMsg;
import com.zy.nut.relayer.common.remoting.RemotingException;
import com.zy.nut.common.beans.exchange.RelayerEnterRoom;
import com.zy.nut.common.beans.exchange.RelayerLeftRoom;
import com.zy.nut.common.beans.exchange.RelayerLogin;
import com.zy.nut.common.beans.exchange.RelayerLogout;

/**
 * Created by Administrator on 2016/11/28.
 */
public interface UserService {
    void login(RelayerLogin relayerLogin);
    void logout(RelayerLogout relayerLogout);

    void enterRoom(RelayerEnterRoom relayerEnterRoom);
    void leftRoom(RelayerLeftRoom relayerLeftRoom);


    void sendDialogMsg(DialogMsg dialogMsg) throws RemotingException;

    void sendRoomMsg(RoomMsg roomMsg) throws RemotingException;
    void sendGlobalMsg() throws RemotingException;

    void dig(long rid) throws RemotingException;
}