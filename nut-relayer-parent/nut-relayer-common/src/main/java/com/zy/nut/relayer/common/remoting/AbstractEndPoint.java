package com.zy.nut.relayer.common.remoting;


import com.zy.nut.relayer.common.URL;
import com.zy.nut.relayer.common.remoting.exchange.header.RelayerCodec;

/**
 * Created by Administrator on 2016/11/6.
 */
public abstract class AbstractEndPoint extends AbstractPeer{
    public static final int     DEFAULT_TIMEOUT                    = 1000;
    public static final int     DEFAULT_CONNECT_TIMEOUT            = 3000;

    private Codec                codec;
    private int                   timeout;
    private int                   connectTimeout;

    public AbstractEndPoint(URL url, ChannelHandler handler) {
        super(url, handler);
        this.codec = getChannelCodec(url);
        this.timeout = DEFAULT_TIMEOUT;
        this.connectTimeout = DEFAULT_CONNECT_TIMEOUT;
    }

    public Codec getCodec() {
        return codec;
    }

    public void setCodec(Codec codec) {
        this.codec = codec;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }


    protected static Codec getChannelCodec(URL url) {
        return new RelayerCodec();
    }
}
