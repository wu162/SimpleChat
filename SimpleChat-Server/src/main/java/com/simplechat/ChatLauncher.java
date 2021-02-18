package com.simplechat;

import com.simplechat.listener.MessageListener;
import com.simplechat.listener.SimpleMessageListener;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChatLauncher {
    private final Logger logger= LoggerFactory.getLogger(getClass());

    private ServerBootstrap bootstrap=null;
    private final NioEventLoopGroup bossGroup=new NioEventLoopGroup(1);
    private final NioEventLoopGroup workerGroup=new NioEventLoopGroup();
    private Channel serverChannel=null;
    private final MessageListener messageListener;

    public ChatLauncher(MessageListener messageListener) {
        this.messageListener=messageListener==null?new SimpleMessageListener():messageListener;
        init();
    }

    public void init(){
        initGateWay();
        bindGateWay();
    }

    private void bindGateWay() {
        try {
            ChannelFuture future = bootstrap.bind(ServerConfig.get().port).sync();
            if(future.isSuccess()){
                logger.info("Server bind port "+ServerConfig.get().port+" successfully");
            }else {
                logger.error("Server fail to bind port "+ServerConfig.get().port);
                logger.error(future.cause().getMessage());
            }

            serverChannel = future.channel();
        } catch (Exception e) {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            e.printStackTrace();
        }

    }

    private void initGateWay(){
        bootstrap=new ServerBootstrap()
                .group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChildInitHandler())
                .option(ChannelOption.SO_BACKLOG,4096)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childAttr(Constants.CHAT_LAUNCHER,this);
    }

    private void shutdown(){
        if(serverChannel!=null){
            try {
                serverChannel.closeFuture().sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
        }
    }

    public MessageListener getMessageListener() {
        return messageListener;
    }

    public static MessageListener AttrListener(ChannelHandlerContext ctx){
        return ctx.channel().attr(Constants.CHAT_LAUNCHER).get().getMessageListener();
    }
}
