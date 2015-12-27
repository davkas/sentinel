package com.davkas.sentinel.remoting.netty;

import io.netty.channel.Channel;

/**
 * Created by hzzhengxianrui on 2015/12/27.
 */
public class NettyEvent {
    private final NettyEventType type;
    private final String remoteAddr;
    private final Channel channel;

    public NettyEvent(NettyEventType type, String remoteAddr, Channel channel) {
        this.type = type;
        this.remoteAddr = remoteAddr;
        this.channel = channel;
    }

    public NettyEventType getType() {
        return type;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public Channel getChannel() {
        return channel;
    }

    @Override
    public String toString() {
        return "NettyEvent{" +
                "type=" + type +
                ", remoteAddr='" + remoteAddr + '\'' +
                ", channel=" + channel +
                '}';
    }
}
