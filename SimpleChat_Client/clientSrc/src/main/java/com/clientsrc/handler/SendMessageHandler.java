package com.clientsrc.handler;

import com.clientsrc.ClientConfig;
import com.clientsrc.Constants;
import com.clientsrc.SendedMessageWrapper;
import com.clientsrc.SimpleChat;
import com.clientsrc.event.LoginEvent;
import com.clientsrc.event.LogoutEvent;
import com.clientsrc.event.RemoveSendedmsgEvent;
import com.clientsrc.event.SendMessageEvent;
import com.clientsrc.message.ChatMessage;
import com.clientsrc.message.ChatMessageFactory;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.EventLoop;

public class SendMessageHandler extends ChannelInboundHandlerAdapter {

    private EventLoop executors = null;

    private final HashMap<String, SendedMessageWrapper> sendedMessageMap = new HashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.executors = ctx.channel().eventLoop();
        super.channelActive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof LoginEvent){
            String userId = ((LoginEvent) evt).userId;
            ChatMessage message = ChatMessageFactory.createLogin(userId);
            ctx.pipeline().writeAndFlush(message);
            ctx.channel().attr(Constants._ATTR_USERID).set(userId);
            ensureSend(message,ctx);
        }else if(evt instanceof LogoutEvent){
            ChatMessage message = ChatMessageFactory.createLogout();
            ctx.pipeline().writeAndFlush(message);
            ctx.channel().attr(Constants._ATTR_USERID).set("");
            ensureSend(message,ctx);
        }else if(evt instanceof SendMessageEvent){
            SendMessageEvent sendMessageEvent = (SendMessageEvent) evt;
            ChatMessage message = ChatMessageFactory.createChatMessage((sendMessageEvent).content,
                    ctx.channel().attr(Constants._ATTR_USERID).get(),
                    sendMessageEvent.to);
            ctx.pipeline().writeAndFlush(message);
            ensureSend(message,ctx);
        }else if(evt instanceof RemoveSendedmsgEvent){
            RemoveSendedmsgEvent clientReceiveEvent = (RemoveSendedmsgEvent) evt;
            ChatMessage message = sendedMessageMap.get(clientReceiveEvent.fp).chatMessage;
            SendedMessageWrapper sendedMessageWrapper = sendedMessageMap.remove(clientReceiveEvent.fp);
            if(sendedMessageWrapper!=null){
                sendedMessageWrapper.scheduledFuture.cancel(true);
            }
        }else{
            super.userEventTriggered(ctx, evt);
        }
    }

    private void ensureSend(ChatMessage message, ChannelHandlerContext ctx) {
        sendedMessageMap.put(message.fp,new SendedMessageWrapper(null,message));
        ensureSend0(message,ctx);
    }

    private void ensureSend0(ChatMessage message, ChannelHandlerContext ctx) {
        message.setRetryCount(message.getRetryCount() + 1);
        if(ctx.channel().isActive() && SimpleChat.getLogin(ctx)){
            sendedMessageMap.get(message.fp).scheduledFuture= executors.schedule(new Runnable() {
                @Override
                public void run() {
                    //超过重试次数，
                    if (message.getRetryCount() >= ClientConfig.retryCount) {
                        return;
                    }
                    ensureSend0(message, ctx);
                }
            }, ClientConfig.resendTimeout, TimeUnit.SECONDS);
        }
    }
}
