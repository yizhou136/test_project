package com.zy.nut.relayer.common.configure;

/**
 * Created by zhougb on 2016/11/10.
 */
public class AMQPConf {
    private String host;
    private int port;
    private String username;
    private String password;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("{host:").append(getHost())
        .append(" post:").append(getPort())
        .append(" username:").append(getUsername())
        .append(" password:").append(getPassword()).append("}");
        return stringBuilder.toString();
    }
}
