package com.simplechat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplechat.message.ChatMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatMessageEncoder extends MessageToByteEncoder<ChatMessage> {

    private Logger logger= LoggerFactory.getLogger(getClass());

    //TODO 多线程有问题吗？
    private static ObjectMapper objectMapper=new ObjectMapper();

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ChatMessage chatMessage, ByteBuf byteBuf) throws Exception {
        byteBuf.writeBytes(objectMapper.writeValueAsString(chatMessage).getBytes());
    }
}
