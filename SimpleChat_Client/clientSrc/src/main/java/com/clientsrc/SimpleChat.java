package com.clientsrc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;

import com.clientsrc.event.LoginEvent;
import com.clientsrc.event.LogoutEvent;
import com.clientsrc.event.SendMessageEvent;
import com.clientsrc.listener.SimpleChatListener;
import com.clientsrc.util.NetworkUtil;

import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public class SimpleChat {

    private boolean init=false;
    private Bootstrap bootstrap;
    private Channel channel;
    private boolean login=false;
    private int connectCount=0;
    private Handler handler;
    private SimpleChatListener listener;
    private Context context;

    static class SimpleChatHolder{
        public static SimpleChat instance=new SimpleChat();
    }

    private SimpleChat() {
    }

    public static SimpleChat get(){
        return SimpleChatHolder.instance;
    }

    //必须在application#onCreate中调用
    public void init(Context context,String serverIP,int serverPort){
        if(!init){
            this.context=context;
            initConfig(serverIP,serverPort);
            init=true;
        }
    }

    public void connect(){
        if(init){
            initThread();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    initSocket();
                    connectToServer();
                }
            });
        }
    }

    private void initThread() {
        HandlerThread handlerThread = new HandlerThread("SimpleChat-Thread");
        handler=new Handler(handlerThread.getLooper());
    }

    public void connectToServer() {
        ChannelFuture channelFuture = bootstrap.connect(ClientConfig.serverIP, ClientConfig.serverPort);
        this.channel=channelFuture.channel();
        channelFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(future.isSuccess()){
                    connectCount=0;
                    //成功
                }else{
                    connectCount+=1;
                    channel.eventLoop().schedule(new Runnable() {
                        @Override
                        public void run() {
                            if(connectCount>=ClientConfig.reconnectCount){
                                //超过重连接尝试次数
                                return;
                            }
                            connectToServer();
                        }
                    },ClientConfig.reconnectInterval, TimeUnit.SECONDS);
                }
            }
        });
        channel.closeFuture().addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if(future.channel()!=null){
                    future.channel().eventLoop().shutdownGracefully();
                }
            }
        });
    }

    private void initConfig(String serverIP, int serverPort) {
        ClientConfig.serverIP=serverIP;
        ClientConfig.serverPort=serverPort;
    }

    private void initSocket() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            bootstrap = new Bootstrap()
                    .group(group)
                    .handler(new HandlerInit())
                    .option(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5 * 1000)
                    .attr(Constants._ATTR_SIMPLECHAT,this);
        }catch (Exception e){
            //发送错误
        }finally {
            group.shutdownGracefully();
        }
    }

    public void sendMessage(String content,String to){
        handler.post(new Runnable() {
            @Override
            public void run() {
                channel.pipeline().fireUserEventTriggered(new SendMessageEvent(content,to));
            }
        });
    }

    public void getLogin(String userId){
        handler.post(new Runnable() {
            @Override
            public void run() {
                channel.pipeline().fireUserEventTriggered(new LoginEvent(userId));
            }
        });
    }

    public void logout(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                channel.pipeline().fireUserEventTriggered(new LogoutEvent());
            }
        });
    }

    public void disconnect(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                channel.close();
            }
        });
    }

    static class NetWorkStateReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

    public static boolean getLogin(ChannelHandlerContext ctx){
        return ctx.channel().attr(Constants._ATTR_SIMPLECHAT).get().login;
    }

    public static void setLogin(ChannelHandlerContext ctx,boolean login){
        SimpleChat.get().setLogin(login);
    }

    private void setLogin(boolean login) {
        this.login=login;
    }

    public void reconnect(String cause) {
        if(NetworkUtil.isNetworkAvailable(context)){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    channel.eventLoop().shutdownGracefully();
                    try {
                        channel.close().sync();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    init=false;
                    login=false;
                    init(context,ClientConfig.serverIP,ClientConfig.serverPort);
                }
            });
        }else{

        }
    }

    public static SimpleChatListener SimpleChatListener(ChannelHandlerContext ctx){
        return ctx.channel().attr(Constants._ATTR_SIMPLECHAT).get().listener;
    }
}
