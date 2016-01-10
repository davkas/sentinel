package com.davkas.sentinel.disruptor.producer;

import com.davkas.sentinel.disruptor.entity.LongEvent;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

/**
 * Created by hzzhengxianrui on 2016/1/9.
 */
public class LongEventProducerWithTranslator {
    private static final EventTranslatorOneArg<LongEvent,ByteBuffer> TRANSLATOR =
            new EventTranslatorOneArg<LongEvent, ByteBuffer>() {
                public void translateTo(LongEvent longEvent, long l, ByteBuffer byteBuffer) {
                    longEvent.setValue(byteBuffer.getLong(0));
                }
            };
    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducerWithTranslator(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(ByteBuffer byteBuffer){
        ringBuffer.publishEvent(TRANSLATOR,byteBuffer);
    }
}
