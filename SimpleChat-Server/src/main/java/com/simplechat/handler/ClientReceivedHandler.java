package com.simplechat.handler;

import com.simplechat.ChatLauncher;
import com.simplechat.event.ClientReceiveEvent;
import com.simplechat.message.ChatMessage;
import com.simplechat.message.ChatMessageType;
import io.netty.channel.ChannelHandlerContext;

public class ClientReceivedHandler extends AbsChatMessageHandler {
    @Override
    protected void processMessage(ChannelHandlerContext ctx, ChatMessage message) {
        ctx.pipeline().fireUserEventTriggered(new ClientReceiveEvent(message.fp));
    }

    @Override
    protected boolean acceptMessage(int type) {
        return type== ChatMessageType.CLIENT_RECEIVED_BACK;
    }
}
