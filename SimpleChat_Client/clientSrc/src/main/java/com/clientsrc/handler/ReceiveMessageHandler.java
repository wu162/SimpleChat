package com.clientsrc.handler;

import com.clientsrc.SimpleChat;
import com.clientsrc.event.RemoveSendedmsgEvent;
import com.clientsrc.message.ChatMessage;
import com.clientsrc.message.ChatMessageType;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ReceiveMessageHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ChatMessage message = (ChatMessage) msg;
        if(message.type== ChatMessageType.SERVER_LOGIN_SUCCESS){
            SimpleChat.setLogin(ctx,true);
            ctx.pipeline().fireUserEventTriggered(new RemoveSendedmsgEvent(message.fp));
            SimpleChat.SimpleChatListener(ctx).onLoginSuccess();
        }else if(message.type==ChatMessageType.SERVER_LOGOUT_SUCCESS){
            SimpleChat.setLogin(ctx,false);
            ctx.pipeline().fireUserEventTriggered(new RemoveSendedmsgEvent(message.fp));
            SimpleChat.SimpleChatListener(ctx).onLogoutSuccess();
        }else if(message.type==ChatMessageType.SERVER_RECEIVED_BACK){
            ctx.pipeline().fireUserEventTriggered(new RemoveSendedmsgEvent(message.fp));
        }else if(message.type==ChatMessageType.CLIENT_TO_CLIENT){
            SimpleChat.SimpleChatListener(ctx).onReceiveMessage(message);
        }else{
            super.channelRead(ctx, msg);
        }
    }
}
