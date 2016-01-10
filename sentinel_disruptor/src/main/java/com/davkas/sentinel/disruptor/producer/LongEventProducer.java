package com.davkas.sentinel.disruptor.producer;

import com.davkas.sentinel.disruptor.entity.LongEvent;
import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * Created by hzzhengxianrui on 2016/1/9.
 */
public class LongEventProducer {
    private  final RingBuffer<LongEvent> ringBuffer;
    public LongEventProducer(RingBuffer<LongEvent> ringBuffer){
        this.ringBuffer = ringBuffer;
    }

    public void onData(ByteBuffer byteBuffer) {

        long sequence = 0;
        try {
            sequence = ringBuffer.next();
            LongEvent event = ringBuffer.get(sequence);
            event.setValue(byteBuffer.getLong(0));
        } finally {
            ringBuffer.publish(sequence);
        }
    }




}
