package com.davkas.sentinel.remoting.netty;

import com.davkas.sentinel.remoting.protocol.RemotingCommand;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import java.nio.ByteBuffer;

/**
 * Created by hzzhengxianrui on 2015/12/27.
 */
public class NettyDecoder extends LengthFieldBasedFrameDecoder{

    private static final int FRAME_MAX_LENGTH = //
            Integer.parseInt(System.getProperty("com.rocketmq.remoting.frameMaxLength", "8388608"));


    public NettyDecoder(){
        super(FRAME_MAX_LENGTH,0,4,0,4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        ByteBuf frame = null;
        frame = (ByteBuf) super.decode(ctx, in);
        if (null == frame) {
            return null;
        }

        ByteBuffer byteBuffer = frame.nioBuffer();
        return RemotingCommand.



    }
}
