package com.clientsrc;

import com.clientsrc.handler.HeartBeatHandler;
import com.clientsrc.handler.ReceiveMessageHandler;
import com.clientsrc.handler.SendMessageHandler;
import com.clientsrc.handler.ServerStateHandler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

class HandlerInit extends ChannelInitializer<Channel> {

    public static int FRAME_FIXED_HEADER_LENGTH = 4;     // 4 bytes
    public static int FRAME_MAX_BODY_LENGTH = 6 * 1024; // 6K bytes

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(
                FRAME_FIXED_HEADER_LENGTH + FRAME_MAX_BODY_LENGTH
                , 0, FRAME_FIXED_HEADER_LENGTH, 0, FRAME_FIXED_HEADER_LENGTH));
        pipeline.addLast(new IdleStateHandler(ClientConfig.readTimeout,ClientConfig.writeTimeout,0));
        pipeline.addLast(new HeartBeatHandler());
        pipeline.addLast(new SendMessageHandler());
        pipeline.addLast(new ReceiveMessageHandler());
        pipeline.addLast(new ServerStateHandler());
    }
}
