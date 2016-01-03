package com.davkas.sentinel.remoting.exception;

/**
 * RPC调用超时异常
 * Created by hzzhengxianrui on 2015/12/27.
 */
public class RemotingTimeoutException extends RemotingException{


    public RemotingTimeoutException(String message) {
        super(message);
    }

    public RemotingTimeoutException(String addr,long timeoutMillis){
            this(addr,timeoutMillis,null);
    }

    public RemotingTimeoutException(String addr,long timeoutMillis,Throwable cause){
        super("wait response on the channel <"+addr+">timeout,"+timeoutMillis+"(ms)");
    }
}
