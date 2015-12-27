package com.davkas.sentinel.remoting.netty;

import com.davkas.sentinel.remoting.ChannelEventListener;
import com.davkas.sentinel.remoting.RPCHOOK;
import com.davkas.sentinel.remoting.common.Pair;
import com.davkas.sentinel.remoting.common.RemotingHelper;
import com.davkas.sentinel.remoting.common.ServiceThread;
import com.davkas.sentinel.remoting.protocol.RemotingCommand;
import io.netty.channel.ChannelHandlerContext;

import java.util.HashMap;
import java.util.concurrent.*;

/**
 * Server与client的共用抽象类
 * Created by hzzhengxianrui on 2015/12/27.
 */
public abstract class NettyRemotingAbstract {

    protected  final Semaphore seamphoreOneWay;

    protected  final Semaphore seamphoreAsync;

    protected  final ConcurrentHashMap<Integer,ResponseFuture> responseTable =
            new ConcurrentHashMap<Integer, ResponseFuture>(256);

    protected Pair<NettyRequestProcessor, ExecutorService> defaultRequestProcessor;

    //注册的各个RPC处理器
    protected final HashMap<Integer/*request code*/,Pair<NettyRequestProcessor,ExecutorService>> processorTable =
            new HashMap<Integer, Pair<NettyRequestProcessor, ExecutorService>>(64);

    protected final NettyEventExecuter nettyEventExecuter = new NettyEventExecuter();

    public abstract ChannelEventListener getChannelEventListener();

    public abstract RPCHOOK getRPCHook();

    public NettyRemotingAbstract(final  int permitsOneWay,final int permitsAsync) {
        this.seamphoreOneWay = new Semaphore(permitsOneWay,true);
        this.seamphoreAsync = new Semaphore(permitsAsync,true);
    }

    public void processRequestCommand(final ChannelHandlerContext ctx,final RemotingCommand cmd){
        final Pair<NettyRequestProcessor,ExecutorService> matched = this.processorTable.get(cmd.getCode);
        final Pair<NettyRequestProcessor,ExecutorService> pair =
                null == matched ? this.defaultRequestProcessor:matched;
        if(pair != null){
            Runnable run = new Runnable() {
                public void run() {
                    RPCHOOK rpchook = NettyRemotingAbstract.this.getRPCHook();
                    if (rpchook != null) {
                        rpchook
                                .doBeforeRequest(RemotingHelper.parseChannelremoteAddr(ctx.channel()), cmd);
                    }
                    final RemotingCommand response = pair.getObject1().processRequest(ctx,cmd);
                    if(rpchook != null){
                        rpchook.doAfterResponse(RemotingHelper.parseChannelRemoteAddr(ctx.channel()),cmd,response);
                    }
                    if(!cmd.isOneWayRPC()){
                        if(response != null){

                        }
                    }
                }
            }
        }
    }

    private class NettyEventExecuter extends ServiceThread{

        private final LinkedBlockingDeque<NettyEvent> eventQueue = new LinkedBlockingDeque<NettyEvent>();
        private final int MaxSize = 10000;

        public void putNettyEvent(final NettyEvent event){
            if(this.eventQueue.size()<=MaxSize){
                eventQueue.add(event);
            }
            else{

            }
        }

        @Override
        protected String getServiceName() {
            return NettyEventExecuter.class.getSimpleName();
        }

        public void run() {
            final ChannelEventListener listener = NettyRemotingAbstract.this.getChannelEventListener();

            while(!this.isStoped()){
                try {
                    NettyEvent event = this.eventQueue.poll(3000, TimeUnit.MILLISECONDS);
                    if(event!=null && listener !=null){
                        switch (event.getType()){
                            case IDLE:
                                break;
                            case CLOSE:
                                break;
                            case CONNECT:
                                break;
                            case EXCEPTION:
                                break;
                            default:
                                break;
                        }
                    }
                }catch(Exception e){

                }
            }
        }
    }


}
