package com.zy.nut.relayer.server.configuration.relayer;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.net.URL;


/**
 * Created by Administrator on 2016/12/4.
 */
@ConfigurationProperties(
        prefix = "relayer"
)
public class RelayerProperties {
    private String configure;

    public String getConfigure() {
        return configure;
    }

    public Resource getConfigureResource(){
        return new PathMatchingResourcePatternResolver().getResource(getConfigure());
    }

    public URL getConfigureUrl(){
        try {
            return getConfigureResource().getURL();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setConfigure(String configure) {
        this.configure = configure;
    }
}
