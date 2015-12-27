package com.davkas.sentinel.remoting.common;

/**
 * Created by hzzhengxianrui on 2015/12/27.
 */
public class RemotingHelper {
    public static final String RemotingLogName = "davkasRemoting";

    public static String exceptionSimpleDesc(final Throwable e){
        StringBuilder sb = new StringBuilder();
        if(e != null){
            sb.append(e.toString());
            StackTraceElement[] stackTrace = e.getStackTrace();
            if(stackTrace != null && stackTrace.length>0){
                StackTraceElement elment = stackTrace[0];
                sb.append(", ");
                sb.append(elment.toString());
            }
        }
        return sb.toString();
    }

}
