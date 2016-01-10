package com.davkas.sentinel.disruptor.main;

import com.davkas.sentinel.disruptor.entity.LongEvent;
import com.lmax.disruptor.dsl.Disruptor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by hzzhengxianrui on 2016/1/9.
 */
public class LongEventMain8 {

    public static void main(String[] args) {
        Executor executor = Executors.newCachedThreadPool();
        int bufferSize = 1024;
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(LongEvent::new,bufferSize,executor);
    }

}
