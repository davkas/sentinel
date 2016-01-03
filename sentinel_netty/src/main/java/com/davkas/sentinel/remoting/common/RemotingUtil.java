package com.davkas.sentinel.remoting.common;

import com.sun.org.apache.bcel.internal.generic.Select;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;

import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * 网络相关方法
 * Created by hzzhengxianrui on 2015/12/27.
 */
public class RemotingUtil {
    public static final String OS_NAME = System.getProperty("os.name");

    private static boolean isLinuxPlatform = false;
    private static boolean isWindowsPlatform = false;

    public static boolean isLinuxPlatform() {
        return isLinuxPlatform;
    }

    public static boolean isWindowsPlatform() {
        return isWindowsPlatform;
    }

    static{
        if(OS_NAME != null && OS_NAME.toLowerCase().indexOf("linux") >=0){
            isLinuxPlatform = true;
        }
        if(OS_NAME != null && OS_NAME.toLowerCase().indexOf("windows") >=0){
            isWindowsPlatform = true;
        }
    }

    public static Selector openSelector() throws IOException {
        Selector result = null;
        if(isLinuxPlatform()){
            try {
                final Class<?> providerClazz = Class.forName("sun.nio.ch.EPollSelectorProvider");

                if(providerClazz != null){
                    try {
                        final Method method = providerClazz.getMethod("provider");
                        if(method != null){
                            final SelectorProvider selectorProvider = (SelectorProvider)method.invoke(null);
                            if(selectorProvider != null){
                                result = selectorProvider.openSelector();
                            }
                        }
                    }catch(final Exception e){
                        e.printStackTrace();
                    }
                }
            } catch (final ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        if(result == null){
            result = Selector.open();
        }
        return result;
    }

    /**
     * 获取本地地址
     * @return
     */
    public static String getLocalAddress(){
        try {
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            ArrayList<String> ipv4Result = new ArrayList<String>();
            ArrayList<String> ipv6Result = new ArrayList<String>();
            while (enumeration.hasMoreElements()){
                final NetworkInterface networkInterface = enumeration.nextElement();
                final Enumeration<InetAddress> en = networkInterface.getInetAddresses();
                while (en.hasMoreElements()){
                    final InetAddress address = en.nextElement();
                    if(!address.isLinkLocalAddress()){
                        if(address instanceof Inet6Address) {
                            ipv6Result.add(normalizeHostAddress(address));
                        }else{
                            ipv4Result.add(normalizeHostAddress(address));
                        }
                    }
                }
            }

            if(!ipv4Result.isEmpty()){
                for(String ip : ipv4Result){
                    if(ip.startsWith("127.0") || ip.startsWith("192.168")){
                        continue;
                    }
                    return ip;
                }
                return ipv4Result.get(ipv4Result.size()-1);
            }else if(!ipv6Result.isEmpty()){
                return ipv6Result.get(0);
            }

            final InetAddress localhost = InetAddress.getLocalHost();
            return normalizeHostAddress(localhost);

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String normalizeHostAddress(final InetAddress localhost){
        if(localhost instanceof InetAddress){
            return "["+localhost.getHostAddress()+"]";
        }else {
            return localhost.getHostAddress();
        }
    }

    /**
     * IP:port
     * @param addr
     * @return
     */
    public static SocketAddress string2SocketAddress(final String addr){
        String[] s = addr.split(":");
        InetSocketAddress isa = new InetSocketAddress(s[0],Integer.valueOf(s[1]));
        return isa;
    }

    public static String socketAddress2String(final SocketAddress addr){
        StringBuilder sb = new StringBuilder();
        InetSocketAddress inetSocketAddress = (InetSocketAddress) addr;
        sb.append(inetSocketAddress.getAddress().getHostAddress());
        sb.append(":");
        sb.append(inetSocketAddress.getPort());
        return sb.toString();
    }

    public static SocketChannel connect(SocketAddress remote){
        return connect(remote, 1000 * 5);
    }

    public static SocketChannel connect(SocketAddress remote,final int timeoutMillis){
        SocketChannel sc = null;
        try {
            sc = SocketChannel.open();
            sc.configureBlocking(true);
            sc.socket().setSoLinger(false, -1);
            sc.socket().setTcpNoDelay(true);
            sc.socket().setReceiveBufferSize(1024 * 64);
            sc.socket().setSendBufferSize(1024 * 64);
            sc.socket().connect(remote, timeoutMillis);
            sc.configureBlocking(false);
            return sc;
        } catch (IOException e) {
            if(sc != null){
                try {
                    sc.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void closeChannel(Channel channel){
        final String addrRemote = RemotingHelper.parseChannelremoteAddr((io.netty.channel.Channel) channel);
        channel.close().addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture channelFuture) throws Exception {

            }
        });
     }

}
