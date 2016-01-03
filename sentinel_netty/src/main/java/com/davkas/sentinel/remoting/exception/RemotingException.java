package com.davkas.sentinel.remoting.exception;

/**
 * 通信类异常父类
 * Created by hzzhengxianrui on 2015/12/27.
 */
public class RemotingException extends Exception {

    public RemotingException(String message){
        super(message);
    }

    public RemotingException(String message, Throwable cause) {
        super(message,cause);
    }
}
