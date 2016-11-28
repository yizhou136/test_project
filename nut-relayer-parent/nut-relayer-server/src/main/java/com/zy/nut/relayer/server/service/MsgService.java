package com.zy.nut.relayer.server.service;

import com.zy.nut.relayer.common.beans.DialogMsg;
import com.zy.nut.relayer.common.beans.RoomMsg;

/**
 * Created by Administrator on 2016/11/28.
 */
public interface MsgService {

    void sendDialogMsg(DialogMsg dialogMsg);

    void sendRoomMsg(RoomMsg roomMsg);

    void sendGlobalMsg();
}
