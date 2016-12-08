package com.zy.nut.web.test.java;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * Created by Administrator on 2016/12/8.
 */
public class RedisMiss<T> {
    private static final byte INIT = 0;
    private static final byte DBLOADED = 1;
    private static final byte EXPIRED = 2;
    private volatile byte state = INIT;
    private AtomicInteger atomicInteger = new AtomicInteger();
    private Instant expiredInstant;
    private T data;

    public void reset(){
        state = INIT;
        setData(null);
    }

    public boolean isDataValid(){
        if (getState() == DBLOADED)
            return true;

        return false;
    }

    public void loadDataSuccessed(T t){
        synchronized (this){
            state = DBLOADED;
            data = t;
            this.notifyAll();
        }
    }

    public void loadDataFailured(){
        synchronized (this){
            state = INIT;
            data = null;
            this.notifyAll();
        }
    }

    public T waitForLoadData(){
        if (!isDataValid()) {
            synchronized (this) {
                try {
                    if (!isDataValid()) {
                        this.wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int inc(){
        return atomicInteger.getAndIncrement();
    }

    public int dec(){
        return atomicInteger.decrementAndGet();
    }

    public byte getState() {
        return state;
    }

    public void setState(byte state) {
        this.state = state;
    }

    public Instant getExpiredInstant() {
        return expiredInstant;
    }

    public void setExpiredInstant(Instant expiredInstant) {
        this.expiredInstant = expiredInstant;
    }
}
