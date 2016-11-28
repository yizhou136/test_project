package com.zy.nut.relayer.server.service;

/**
 * Created by Administrator on 2016/11/28.
 */
public interface UserService {

    void regUid(long uid);
    void unregUid(long uid);

    void enterRoom(long uid, long rid);
    void leftRoom(long uid, long rid);
}
