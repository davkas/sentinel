package com.davkas.sentinel.disruptor.handler;

import com.davkas.sentinel.disruptor.entity.LongEvent;
import com.lmax.disruptor.EventHandler;

/**
 * Created by hzzhengxianrui on 2016/1/9.
 */
public class LongEventHandler implements EventHandler<LongEvent>{
    public void onEvent(LongEvent o, long l, boolean b) throws Exception {
        System.out.println(o.getValue());
    }
}
