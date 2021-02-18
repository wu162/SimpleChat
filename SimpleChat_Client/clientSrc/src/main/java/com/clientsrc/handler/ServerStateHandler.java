package com.clientsrc.handler;

import com.clientsrc.Constants;
import com.clientsrc.event.ServerLostEvent;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerStateHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if(evt instanceof ServerLostEvent){
            ctx.channel().attr(Constants._ATTR_SIMPLECHAT).get().reconnect("长时间未接收到服务器心跳包");
        }else{
            super.userEventTriggered(ctx, evt);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().attr(Constants._ATTR_SIMPLECHAT).get().reconnect("未知原因");
        super.channelInactive(ctx);
    }
}
