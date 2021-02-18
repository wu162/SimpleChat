package com.simplechat.listener;

import com.simplechat.message.ChatMessage;

public interface MessageListener {
    //客户端发送登录消息时
    void onClientLogin(String userId);

    //客户端退出时
    void onClientLogout(String userId);

    //收到客户端到客户端的消息，只会在重试次数为0时调用
    void onC2CMessage(ChatMessage chatMessage);

    //收到客户端ack应答
    void onClientReceivedMessage(ChatMessage chatMessage);

    //客户端给服务端的消息
    void onC2SMessage(ChatMessage chatMessage);

    //重试次数超过上限
    void onC2CMessageLost(ChatMessage chatMessage);

    //客户端到客户端的消息，但是对方不在线
    void onC2CMessageToClientLost(ChatMessage chatMessage);
}
