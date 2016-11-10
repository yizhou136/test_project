package com.zy.nut.relayer.common.container;

import com.zy.nut.relayer.common.configure.Configuration;
import com.zy.nut.relayer.common.configure.ConfigurationLoader;

import java.net.URL;

/**
 * Created by Administrator on 2016/11/7.
 */
public abstract class AbstractContainer implements Container{
    private Configuration configuration;
    private URL propertiesUrl;
    private String serverName;

    public AbstractContainer(URL propertiesUrl) throws Throwable{
        this.propertiesUrl = propertiesUrl;
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

    public void start() {

    }

    public void reconfigure(String newPropertiesUrl) {

    }

    public void stop() {

    }


    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
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
