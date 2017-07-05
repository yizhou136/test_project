package com.zy.nut.common.beans;

/*import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;*/

/**
 * Created by Administrator on 2016/11/28.
 */
//@Entity
public class Room {
/*    @Id
    @GeneratedValue*/
    private long rid;

    private String title;
    private String rtmpUrl;
    private long ctime;

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRtmpUrl() {
        return rtmpUrl;
    }

    public void setRtmpUrl(String rtmpUrl) {
        this.rtmpUrl = rtmpUrl;
    }

    public long getCtime() {
        return ctime;
    }

    public void setCtime(long ctime) {
        this.ctime = ctime;
    }
}
