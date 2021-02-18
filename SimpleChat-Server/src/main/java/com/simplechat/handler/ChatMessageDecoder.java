package com.simplechat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simplechat.message.ChatMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ChatMessageDecoder extends ByteToMessageDecoder {

    private static ObjectMapper objectMapper=new ObjectMapper();

    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if(byteBuf.readableBytes()>0){
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(bytes);
            String jsonStr = new String(bytes);
            list.add(objectMapper.readValue(jsonStr, ChatMessage.class));
        }
    }
}
