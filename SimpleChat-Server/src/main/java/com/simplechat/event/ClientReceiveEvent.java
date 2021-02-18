package com.simplechat.event;

public class ClientReceiveEvent {
    public String MessageFp;

    public ClientReceiveEvent(String messageFp) {
        MessageFp = messageFp;
    }
}
