package com.zy.nut.common.beans;

/*import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;*/

/**
 * Created by Administrator on 2016/11/28.
 */
public class Boker {
/*    @Id
    @GeneratedValue*/
    private long uid;

    private long beans;

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getBeans() {
        return beans;
    }

    public void setBeans(long beans) {
        this.beans = beans;
    }
}
