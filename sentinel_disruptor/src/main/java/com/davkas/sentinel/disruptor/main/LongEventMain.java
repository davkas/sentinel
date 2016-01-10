package com.davkas.sentinel.disruptor.main;

import com.davkas.sentinel.disruptor.entity.LongEvent;
import com.davkas.sentinel.disruptor.factory.LongEventFacotry;
import com.davkas.sentinel.disruptor.handler.LongEventHandler;
import com.davkas.sentinel.disruptor.producer.LongEventProducer;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;

import java.nio.ByteBuffer;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by hzzhengxianrui on 2016/1/9.
 */
public class LongEventMain {

    public static void main(String[] args) throws InterruptedException {
        Executor executor = Executors.newCachedThreadPool();
        LongEventFacotry facotry = new LongEventFacotry();
        int bufferSize = 1024;
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(facotry, bufferSize, executor);
        disruptor.handleEventsWith(new LongEventHandler());
        disruptor.start();
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        LongEventProducer producer = new LongEventProducer(ringBuffer);
        ByteBuffer bb = ByteBuffer.allocate(8);
        for (int i = 0; true; i++) {
            bb.putLong(0,1);
            producer.onData(bb);
            TimeUnit.SECONDS.sleep(1);
        }

    }

}
