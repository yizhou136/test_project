package com.zy.nut.relayer.common.container;

import com.zy.nut.relayer.common.configure.Configuration;
import com.zy.nut.relayer.common.configure.ConfigurationLoader;

/**
 * Created by Administrator on 2016/11/7.
 */
public abstract class AbstractContainer implements Container{
    private Configuration configuration;
    private String propertiesUrl;

    public AbstractContainer(String propertiesUrl){
        this.propertiesUrl = propertiesUrl;
        configure(propertiesUrl);
    }

    public void configure(String propertiesUrl) {
        configuration = ConfigurationLoader.load(propertiesUrl);
    }

    public void start() {

    }

    public void reconfigure(String newPropertiesUrl) {

    }

    public void stop() {

    }
}
