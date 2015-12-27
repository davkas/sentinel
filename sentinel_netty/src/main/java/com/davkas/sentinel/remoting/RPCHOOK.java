package com.davkas.sentinel.remoting;


import com.davkas.sentinel.remoting.protocol.RemotingCommand;

/**
 * Created by hzzhengxianrui on 2015/12/27.
 */
public interface RPCHOOK {
    public void doBeforeRequest(final String remoteAddr,final RemotingCommand request);
    public void doAfterResponse(final String remoteAddr,final RemotingCommand request,final RemotingCommand response);
}
