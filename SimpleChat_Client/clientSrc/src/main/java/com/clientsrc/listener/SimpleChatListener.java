package com.clientsrc.listener;

import com.clientsrc.message.ChatMessage;

public interface SimpleChatListener {
    void onLoginSuccess();
    void onLogoutSuccess();
    void onSendMessageSuccess(ChatMessage message);
    void onSendMessageFail(ChatMessage message);
    void onConnectFail();
    void onConnectSuccess();
    void onReceiveMessage(ChatMessage message);
}
