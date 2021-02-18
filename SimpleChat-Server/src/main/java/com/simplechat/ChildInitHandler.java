package com.simplechat;

import com.simplechat.handler.ChatMessageDecoder;
import com.simplechat.handler.ChatMessageEncoder;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

public class ChildInitHandler extends ChannelInitializer<Channel> {

    public static int FRAME_FIXED_HEADER_LENGTH = 4;     // 4 bytes
    public static int FRAME_MAX_BODY_LENGTH = 6 * 1024; // 6K bytes

    @Override
    protected void initChannel(Channel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();
        pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(
                FRAME_FIXED_HEADER_LENGTH + FRAME_MAX_BODY_LENGTH
                , 0, FRAME_FIXED_HEADER_LENGTH, 0, FRAME_FIXED_HEADER_LENGTH));
        pipeline.addLast("frameEncoder", new LengthFieldPrepender(FRAME_FIXED_HEADER_LENGTH));
        pipeline.addLast(new IdleStateHandler(ServerConfig.get().readTimeout,ServerConfig.get().writeTimeout,0));

        pipeline.addLast(new ChatMessageDecoder());
        pipeline.addLast(new ChatMessageEncoder());
    }
}
