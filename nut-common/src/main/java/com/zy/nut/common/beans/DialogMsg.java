package com.zy.nut.common.beans;

//import javax.persistence.Entity;

/**
 * Created by Administrator on 2016/11/28.
 */
//@Entity
public class DialogMsg extends BaseMsg{
    private long fuid;
    private long tuid;

    private long proxyReceiveMs;
    private long startDecodeMs;
    private long endDecodeMs;
    private long proxySendMs;

    private long backReceiveMs;
    private long backSendMs;

    private long proxyReceiveBackMs;

    public long getFuid() {
        return fuid;
    }

    public void setFuid(long fuid) {
        this.fuid = fuid;
    }

    public long getTuid() {
        return tuid;
    }

    public void setTuid(long tuid) {
        this.tuid = tuid;
    }

    public long getProxyReceiveMs() {
        return proxyReceiveMs;
    }

    public void setProxyReceiveMs(long proxyReceiveMs) {
        this.proxyReceiveMs = proxyReceiveMs;
    }

    public long getStartDecodeMs() {
        return startDecodeMs;
    }

    public void setStartDecodeMs(long startDecodeMs) {
        this.startDecodeMs = startDecodeMs;
    }

    public long getEndDecodeMs() {
        return endDecodeMs;
    }

    public void setEndDecodeMs(long endDecodeMs) {
        this.endDecodeMs = endDecodeMs;
    }

    public long getProxySendMs() {
        return proxySendMs;
    }

    public void setProxySendMs(long proxySendMs) {
        this.proxySendMs = proxySendMs;
    }

    public long getBackReceiveMs() {
        return backReceiveMs;
    }

    public void setBackReceiveMs(long backReceiveMs) {
        this.backReceiveMs = backReceiveMs;
    }

    public long getBackSendMs() {
        return backSendMs;
    }

    public long getProxyReceiveBackMs() {
        return proxyReceiveBackMs;
    }

    public void setProxyReceiveBackMs(long proxyReceiveBackMs) {
        this.proxyReceiveBackMs = proxyReceiveBackMs;
    }

    public void setBackSendMs(long backSendMs) {
        this.backSendMs = backSendMs;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        long clientToProxyNetMs = getProxyReceiveMs() - getLctime();
        long proxyDecodeEscapeMs = getEndDecodeMs() - getStartDecodeMs();
        long proxyRTEscapeMs = getProxySendMs() - getStartDecodeMs();
        long proxyToBackNetMs = getBackReceiveMs() - getProxySendMs();
        long backRTEscapeMs = getBackSendMs() - getBackReceiveMs();
        long backToClientNetMs = getCtime() - getBackSendMs();
        long totalEscapeMs = getCtime() - getLctime();
        long proxyAndBackRTEscapeMs =  proxyRTEscapeMs + backRTEscapeMs;
        stringBuilder.append("\nclientToProxyNetMs:").append(clientToProxyNetMs)
                .append("\nproxyDecodeEscapeMs:").append(proxyDecodeEscapeMs)
                .append("\nproxyRTEscapeMs:").append(proxyRTEscapeMs)
                .append("\nproxyToBackNetMs:").append(proxyToBackNetMs)
                .append("\nbackRTEscapeMs:").append(backRTEscapeMs)
                .append("\nbackToClientNetMs:").append(backToClientNetMs)
                .append("\ntotalEscapeMs:").append(totalEscapeMs)
                .append("\nproxyAndBackRTEscapeMs:").append(proxyAndBackRTEscapeMs);
        return stringBuilder.toString();
    }
}
