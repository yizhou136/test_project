package com.zy.nut.relayer.common.remoting.exchange;

/**
 * Created by zhougb on 2016/11/9.
 */
public class RelayerLoginLogout {
    private boolean isLogin;
    private long uid;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }
}
