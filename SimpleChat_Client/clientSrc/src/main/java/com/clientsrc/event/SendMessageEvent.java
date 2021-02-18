package com.clientsrc.event;

public class SendMessageEvent {
    public String content;
    public String to;

    public SendMessageEvent(String content, String to) {
        this.content = content;
        this.to = to;
    }
}
