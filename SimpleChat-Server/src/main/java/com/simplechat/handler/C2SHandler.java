package com.simplechat.handler;

import com.simplechat.ChatLauncher;
import com.simplechat.message.ChatMessage;
import com.simplechat.message.ChatMessageFactory;
import com.simplechat.message.ChatMessageType;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class C2SHandler extends AbsChatMessageHandler {
    private Logger logger= LoggerFactory.getLogger(getClass());

    @Override
    protected void processMessage(ChannelHandlerContext ctx, ChatMessage message) {
        logger.info("receive message from client to server "+message);
        ChatLauncher.AttrListener(ctx).onC2SMessage(message);
        ctx.pipeline().writeAndFlush(ChatMessageFactory.createServerReceived(message.fp));
    }

    @Override
    protected boolean acceptMessage(int type) {
        return type== ChatMessageType.CLIENT_TO_SERVER;
    }
}
