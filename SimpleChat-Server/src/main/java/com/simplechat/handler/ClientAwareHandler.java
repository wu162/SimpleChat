package com.simplechat.handler;

import com.simplechat.ChatLauncher;
import com.simplechat.ClientsHolder;
import com.simplechat.Constants;
import com.simplechat.event.ClientLostEvent;
import com.simplechat.listener.MessageListener;
import com.simplechat.message.ChatMessage;
import com.simplechat.message.ChatMessageFactory;
import com.simplechat.message.ChatMessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientAwareHandler extends ChannelInboundHandlerAdapter {

    private Logger logger= LoggerFactory.getLogger(getClass());

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(((ChatMessage)msg).type==ChatMessageType.CLIENT_LOGIN){
            if(ClientsHolder.get().justConnected(ctx.channel())){
                ClientsHolder.get().addClient(ctx.channel(),(ChatMessage)msg);
                ctx.channel().attr(Constants.USER_ID_ATTR).set(((ChatMessage) msg).content);
                ChatLauncher.AttrListener(ctx).onClientLogin(((ChatMessage) msg).content);
            }else{
                //重复的登录消息
            }
            ctx.channel().pipeline().writeAndFlush(ChatMessageFactory.createLoginSuccess());
        }
        if(((ChatMessage)msg).type==ChatMessageType.CLIENT_LOGOUT){
            if(ClientsHolder.get().hasConnected(ctx.channel())){
                removeClient(ctx);
            }else{
                //重复的登出消息
            }
            ctx.channel().pipeline().writeAndFlush(ChatMessageFactory.createLogoutSuccess());
        }
    }

    private void removeClient(ChannelHandlerContext ctx) {
        if(ClientsHolder.get().hasConnected(ctx.channel())){
            MessageListener messageListener = ChatLauncher.AttrListener(ctx);
            String userId = ctx.channel().attr(Constants.USER_ID_ATTR).get();
            ClientsHolder.get().removeClient(userId);
            ctx.close();
            messageListener.onClientLogout(userId);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof ClientLostEvent){
            removeClient(ctx);
        }else{
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        removeClient(ctx);
        super.channelInactive(ctx);
    }
}
