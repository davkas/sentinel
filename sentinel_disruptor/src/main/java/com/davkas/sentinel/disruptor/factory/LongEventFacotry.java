package com.davkas.sentinel.disruptor.factory;

import com.davkas.sentinel.disruptor.entity.LongEvent;
import com.lmax.disruptor.EventFactory;

/**
 * Created by hzzhengxianrui on 2016/1/9.
 */
public class LongEventFacotry implements EventFactory<LongEvent> {
    public LongEvent newInstance() {
        return new LongEvent();
    }
}
