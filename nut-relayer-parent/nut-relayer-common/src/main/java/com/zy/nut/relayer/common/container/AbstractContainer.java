package com.zy.nut.relayer.common.container;

import com.zy.nut.relayer.common.configure.Configuration;
import com.zy.nut.relayer.common.configure.ConfigurationLoader;
import com.zy.nut.relayer.common.remoting.Codec;
import com.zy.nut.relayer.common.remoting.buffer.ChannelBuffer;
import com.zy.nut.relayer.common.remoting.buffer.ChannelBuffers;
import com.zy.nut.relayer.common.remoting.exchange.TransformData;
import com.zy.nut.relayer.common.remoting.exchange.header.RelayerCodec;

import java.net.URL;

/**
 * Created by Administrator on 2016/11/7.
 */
public abstract class AbstractContainer implements Container{
    private Configuration configuration;
    private URL propertiesUrl;
    private String serverName;
    private Codec codec;
    private ChannelBuffer decodedChannelBuffer;

    public AbstractContainer(URL propertiesUrl) throws Throwable{
        this.propertiesUrl = propertiesUrl;
        codec = new RelayerCodec();
        decodedChannelBuffer = ChannelBuffers.buffer(1024);
        configure();
        init();
    }

    public abstract void init() throws Throwable;

    public void configure() {
        configuration = ConfigurationLoader.load(propertiesUrl);
        if (configuration != null) {
            serverName = String.format("%s_%s",
                    configuration.getServerCluster(),
                    configuration.getServerAddress());
        }
    }

    public void start() {}
    public void stop() {}
    //public void receiveFromBackend(byte[] data) {}
    //public void receiveFromServer(byte[] data) {}



    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public Codec getCodec() {
        return codec;
    }

    public void setCodec(Codec codec) {
        this.codec = codec;
    }

    public ChannelBuffer getDecodedChannelBuffer() {
        return decodedChannelBuffer;
    }

    public void setDecodedChannelBuffer(ChannelBuffer decodedChannelBuffer) {
        this.decodedChannelBuffer = decodedChannelBuffer;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public URL getPropertiesUrl() {
        return propertiesUrl;
    }

    public void setPropertiesUrl(URL propertiesUrl) {
        this.propertiesUrl = propertiesUrl;
    }

    protected enum ContainerState {
        CONFIGURING,
        RECONFIGURING,
        VOTING,
        RUNNING,
        STOPED
    }
}
