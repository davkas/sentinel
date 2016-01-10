package com.davkas.sentinel.disruptor.entity;

/**
 * Created by hzzhengxianrui on 2016/1/9.
 */
public class LongEvent {
    private long value;
    public long getValue(){
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }
}
