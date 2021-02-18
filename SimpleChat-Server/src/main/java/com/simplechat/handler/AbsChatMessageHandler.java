package com.simplechat.handler;

import com.simplechat.message.ChatMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public abstract class AbsChatMessageHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ChatMessage message = (ChatMessage) msg;
        if(acceptMessage(message.type)){
            processMessage(ctx,message);
        }else{
            ctx.fireChannelRead(msg);
        }
    }

    protected abstract void processMessage(ChannelHandlerContext ctx, ChatMessage message);

    protected abstract boolean acceptMessage(int type);
}
