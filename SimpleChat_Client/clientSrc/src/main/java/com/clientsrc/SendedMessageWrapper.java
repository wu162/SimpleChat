package com.clientsrc;

import com.clientsrc.message.ChatMessage;

import io.netty.util.concurrent.ScheduledFuture;

public class SendedMessageWrapper {
    public ScheduledFuture<?> scheduledFuture;
    public ChatMessage chatMessage;

    public SendedMessageWrapper(ScheduledFuture<?> scheduledFuture, ChatMessage chatMessage) {
        this.scheduledFuture = scheduledFuture;
        this.chatMessage = chatMessage;
    }
}
