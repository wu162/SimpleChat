package com.simplechat.handler;

import com.simplechat.event.ClientLostEvent;
import com.simplechat.message.ChatMessage;
import com.simplechat.message.ChatMessageFactory;
import com.simplechat.message.ChatMessageType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            if(IdleState.READER_IDLE == ((IdleStateEvent) evt).state()){
                ctx.fireUserEventTriggered(new ClientLostEvent());
                ctx.close();
            }
            if(IdleState.WRITER_IDLE == ((IdleStateEvent) evt).state()){
                ctx.pipeline().writeAndFlush(ChatMessageFactory.createServerKeepAlive());
            }
        }else{
            super.userEventTriggered(ctx, evt);
        }
    }
}
