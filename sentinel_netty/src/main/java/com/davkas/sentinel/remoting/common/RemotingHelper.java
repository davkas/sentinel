package com.davkas.sentinel.remoting.common;

import com.davkas.sentinel.remoting.exception.RemotingSendRequestException;
import com.davkas.sentinel.remoting.exception.RemotingTimeoutException;
import com.davkas.sentinel.remoting.protocol.RemotingCommand;
import io.netty.channel.Channel;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.sql.Time;
import java.util.concurrent.TimeUnit;

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

    /**
     *IP：port
     * @param addr
     * @return
     */
    public static SocketAddress string2SocketAddress(final String addr){
        String[] s = addr.split(":");
        InetSocketAddress isa = new InetSocketAddress(s[0],Integer.valueOf(s[1]));
        return isa;
    }

    public static RemotingCommand invokeSync(final String addr,final RemotingCommand request,
                                             final long timeOutMillis) throws RemotingSendRequestException, InterruptedException, RemotingTimeoutException {
        long beginTime = System.currentTimeMillis();
        SocketAddress socketAddress = RemotingUtil.string2SocketAddress(addr);
        SocketChannel socketChannel = RemotingUtil.connect(socketAddress);
        if(socketChannel != null){
            boolean sendRequestOk = false;
            //使用阻塞模式
            try {
                socketChannel.configureBlocking(true);
                socketChannel.socket().setSoTimeout((int) timeOutMillis);

                //发送数据
                ByteBuffer byteBufferRequest = request.encode();
                while (byteBufferRequest.hasRemaining()){
                    int length = socketChannel.write(byteBufferRequest);
                    if(length > 0){
                        if(byteBufferRequest.hasRemaining()){
                            if((System.currentTimeMillis()-beginTime)>timeOutMillis){
                                //发送请求超时
                                throw new RemotingSendRequestException(addr);
                            }
                        }
                    }else{
                        throw new RemotingSendRequestException(addr);
                    }
                    TimeUnit.MICROSECONDS.sleep(1);
                }

                sendRequestOk = true;
                //接收应答SIZE
                ByteBuffer byteBufferSize = ByteBuffer.allocate(4);
                while (byteBufferSize.hasRemaining()){
                    int length = socketChannel.read(byteBufferSize);
                    if(length>0) {
                        if (byteBufferSize.hasRemaining()) {
                            if ((System.currentTimeMillis() - beginTime) > timeOutMillis) {
                                throw new RemotingTimeoutException(addr, timeOutMillis);
                            }
                        } else {
                            throw new RemotingTimeoutException(addr, timeOutMillis);
                        }
                    }
                    TimeUnit.MICROSECONDS.sleep(1);
                }

                //接收应答body
                int size = byteBufferSize.getInt(0);
                ByteBuffer byteBufferBody = ByteBuffer.allocate(size);
                while(byteBufferBody.hasRemaining()){
                    int length = socketChannel.read(byteBufferBody);
                   if(length>0) {
                       if ((System.currentTimeMillis() - beginTime) > timeOutMillis) {
                           throw new RemotingTimeoutException(addr, timeOutMillis);
                       }
                   }else{
                       throw new RemotingTimeoutException(addr,timeOutMillis);
                   }
                    TimeUnit.MICROSECONDS.sleep(1);
                }

                //对应答数据进行解码
                byteBufferBody.flip();
                return RemotingCommand.decode(byteBufferBody);
            } catch (IOException e) {
                e.printStackTrace();
                if(sendRequestOk){
                    throw new RemotingTimeoutException(addr,timeOutMillis);
                }else{
                    throw new RemotingSendRequestException(addr);
                }
            }finally {
                try{
                    socketChannel.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }
    }


    public static String parseChannelremoteAddr(Channel channel) {
        return null;
    }
}
