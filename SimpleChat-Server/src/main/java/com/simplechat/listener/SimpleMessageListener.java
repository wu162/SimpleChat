package com.simplechat.listener;

import com.simplechat.message.ChatMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleMessageListener implements MessageListener {
    private Logger logger= LoggerFactory.getLogger(getClass());

    @Override
    public void onClientLogin(String userId) {
        logger.info("{}: {}","onClientLogin",userId);
    }

    @Override
    public void onClientLogout(String userId) {
        logger.info("{}: {}","onClientLogout",userId);
    }

    @Override
    public void onC2CMessage(ChatMessage chatMessage) {
        logger.info("{}: {}","onC2CMessage",chatMessage.toString());
    }

    @Override
    public void onClientReceivedMessage(ChatMessage chatMessage) {
        logger.info("{}: {}","onClientReceivedMessage",chatMessage.toString());
    }

    @Override
    public void onC2SMessage(ChatMessage chatMessage) {
        logger.info("{}: {}","onC2SMessage",chatMessage.toString());
    }

    @Override
    public void onC2CMessageLost(ChatMessage chatMessage) {
        logger.info("{}: {}","onC2CMessageLost",chatMessage.toString());
    }

    @Override
    public void onC2CMessageToClientLost(ChatMessage chatMessage) {
        logger.info("{}: {}","onC2CMessageToClientLost",chatMessage.toString());
    }
}
