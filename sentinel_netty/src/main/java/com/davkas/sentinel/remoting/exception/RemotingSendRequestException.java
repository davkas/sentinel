package com.davkas.sentinel.remoting.exception;

/**
 * Created by hzzhengxianrui on 2015/12/27.
 */
public class RemotingSendRequestException extends RemotingException {

    public RemotingSendRequestException(String addr){
        this(addr,null);
    }

    public RemotingSendRequestException(String addr,Throwable cause){
        super("send request to <"+addr+"> failed",cause);
    }
}
