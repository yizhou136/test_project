package com.zy.nut.relayer.common.remoting;

import com.zy.nut.relayer.common.URL;
import com.zy.nut.relayer.common.configure.Configuration;
import com.zy.nut.relayer.common.remoting.exchange.header.RelayerCodec;
import com.zy.nut.relayer.common.utils.NetUtils;

import java.net.InetSocketAddress;

/**
 * Created by Administrator on 2016/11/6.
 */
public abstract class AbstractEndPoint{
    public static final int     DEFAULT_TIMEOUT                    = 1000;
    public static final int     DEFAULT_CONNECT_TIMEOUT            = 5000;

    private volatile boolean     closed;
    private Codec                codec;
    private int                  timeout;
    private int                  connectTimeout;
    private Configuration        configuration;

    private InetSocketAddress    bindAddress;
    private InetSocketAddress    localAddress;
    private InetSocketAddress    remoteAddress;
    private URL                  parsedURL;

    //private boolean              isClusterLeader;

    public AbstractEndPoint(Configuration configuration) {
        this.configuration = configuration;
        this.codec = getChannelCodec(configuration);
        this.timeout = DEFAULT_TIMEOUT;
        this.connectTimeout = DEFAULT_CONNECT_TIMEOUT;

        if(configuration.isServerClient()){
            parsedURL = getConfiguration().getClusterServerAddressParsedURL();
            remoteAddress = new InetSocketAddress(NetUtils.filterLocalHost(parsedURL.getHost()), parsedURL.getPort());
            localAddress = InetSocketAddress.createUnresolved(NetUtils.getLocalHost(), 0);
        }else {
            parsedURL = getConfiguration().getServerAddressParsedURL();
            localAddress = new InetSocketAddress(parsedURL.getHost(), parsedURL.getPort());
            String host = NetUtils.isInvalidLocalHost(getURL().getHost())
                    ? NetUtils.ANYHOST : getURL().getHost();
            bindAddress = new InetSocketAddress(host, getURL().getPort());
        }
    }

    public void close() {
        closed = true;
    }

    public boolean isClosed() {
        return closed;
    }
    public void setClosed(boolean closed) {
        this.closed = closed;
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

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public InetSocketAddress getLocalAddress(){
        return localAddress;
    }

    public InetSocketAddress getRemoteAddress(){
        return remoteAddress;
    }

    public InetSocketAddress getBindAddress() {
        return bindAddress;
    }

    public void setBindAddress(InetSocketAddress bindAddress) {
        this.bindAddress = bindAddress;
    }

    public void setLocalAddress(InetSocketAddress localAddress) {
        this.localAddress = localAddress;
    }

    public void setRemoteAddress(InetSocketAddress remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public URL getParsedURL() {
        return parsedURL;
    }

    public void setParsedURL(URL parsedURL) {
        this.parsedURL = parsedURL;
    }

    public String getServerAddress(){
        return getConfiguration().getServerAddress();
    }

    public URL getURL(){
        return parsedURL;
    }

    protected static Codec getChannelCodec(Configuration configuration) {
        return new RelayerCodec();
    }
}
