package com.zy.nut.common.msp;

import com.zy.nut.common.beans.DialogMsg;
import com.zy.nut.common.beans.RoomMsg;

/**
 * Created by zhougb on 2017/2/24.
 */
public interface MsProxyService {

    Response sendTo(DialogMsg dialogMsg);

    Response publish(RoomMsg roomMsg);
}