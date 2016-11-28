package com.zy.nut.relayer.server.configuration.datasource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by Administrator on 2016/10/30.
 */
@ConfigurationProperties(
        prefix = "nut.datasource"
)
public class NutDataSourceProperties{
    private String roUrl;
    private String roUsername;
    private String roPassword;

    private String rwUrl;
    private String rwUsername;
    private String rwPassword;


    public String getRoUrl() {
        return roUrl;
    }

    public void setRoUrl(String roUrl) {
        this.roUrl = roUrl;
    }

    public String getRoUsername() {
        return roUsername;
    }

    public void setRoUsername(String roUsername) {
        this.roUsername = roUsername;
    }

    public String getRoPassword() {
        return roPassword;
    }

    public void setRoPassword(String roPassword) {
        this.roPassword = roPassword;
    }

    public String getRwUrl() {
        return rwUrl;
    }

    public void setRwUrl(String rwUrl) {
        this.rwUrl = rwUrl;
    }

    public String getRwUsername() {
        return rwUsername;
    }

    public void setRwUsername(String rwUsername) {
        this.rwUsername = rwUsername;
    }

    public String getRwPassword() {
        return rwPassword;
    }

    public void setRwPassword(String rwPassword) {
        this.rwPassword = rwPassword;
    }
}
