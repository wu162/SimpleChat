package com.clientsrc.handler;

import com.clientsrc.event.ServerLostEvent;
import com.clientsrc.message.ChatMessageFactory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof IdleStateEvent){
            if(IdleState.READER_IDLE == ((IdleStateEvent) evt).state()){
                ctx.fireUserEventTriggered(new ServerLostEvent());
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
