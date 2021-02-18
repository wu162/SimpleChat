package com.simplechat.handler;

import com.simplechat.ChatLauncher;
import com.simplechat.ClientsHolder;
import com.simplechat.SendedMessageWrapper;
import com.simplechat.ServerConfig;
import com.simplechat.event.ClientReceiveEvent;
import com.simplechat.message.ChatMessage;
import com.simplechat.message.ChatMessageFactory;
import com.simplechat.message.ChatMessageType;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class C2CHandler extends AbsChatMessageHandler {

    private EventLoop executors = null;

    private final HashMap<String, SendedMessageWrapper> sendedMessageMap = new HashMap<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.executors = ctx.channel().eventLoop();
        super.channelActive(ctx);
    }

    @Override
    protected void processMessage(ChannelHandlerContext ctx, ChatMessage message) {
        if (!sendedMessageMap.containsKey(message.fp)) {
            message.setRetryCount(0);
            ChatLauncher.AttrListener(ctx).onC2CMessage(message);
            sendedMessageMap.put(message.fp,new SendedMessageWrapper(null,message));
            processMessage0(message,ctx);
        } else {
            //重复的消息
        }
        ctx.pipeline().writeAndFlush(ChatMessageFactory.createServerReceived(message.fp));
    }

    private void processMessage0(ChatMessage message, ChannelHandlerContext ctx) {
        message.setRetryCount(message.getRetryCount() + 1);
        if (ClientsHolder.get().hasConnected(message.to)) {
            ClientsHolder.get().getClient(message.to).writeAndFlush(message).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        //发送成功
                        sendedMessageMap.get(message.fp).scheduledFuture= executors.schedule(new Runnable() {
                            @Override
                            public void run() {
                                //超过重试次数，
                                if (message.getRetryCount() >= ServerConfig.get().retryCount) {
                                    ChatLauncher.AttrListener(ctx).onC2CMessageLost(message);
                                    return;
                                }
                                processMessage0(message, ctx);
                            }
                        }, ServerConfig.get().resendTimeout, TimeUnit.SECONDS);
                    } else {
                        //发送失败
                    }
                }
            });
        } else {
            ChatLauncher.AttrListener(ctx).onC2CMessageToClientLost(message);
        }
    }

    @Override
    protected boolean acceptMessage(int type) {
        return type == ChatMessageType.CLIENT_TO_CLIENT;
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof ClientReceiveEvent) {
            ClientReceiveEvent clientReceiveEvent = (ClientReceiveEvent) evt;
            ChatMessage message = sendedMessageMap.get(clientReceiveEvent.MessageFp).chatMessage;
            SendedMessageWrapper sendedMessageWrapper = sendedMessageMap.remove(clientReceiveEvent.MessageFp);
            if(sendedMessageWrapper!=null){
                sendedMessageWrapper.scheduledFuture.cancel(true);
            }
            ChatLauncher.AttrListener(ctx).onClientReceivedMessage(message);
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
}
