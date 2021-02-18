package com.simplechat;

import com.simplechat.message.ChatMessage;
import io.netty.channel.Channel;

import java.util.concurrent.ConcurrentHashMap;

public class ClientsHolder {

    private ConcurrentHashMap<String, Channel> clients=new ConcurrentHashMap<String, Channel>();

    private ClientsHolder(){}

    public void removeClient(String s) {
        clients.remove(s);
    }

    static class ClientsHolderHolder{
        static ClientsHolder instance=new ClientsHolder();
    }

    public static ClientsHolder get(){
        return ClientsHolderHolder.instance;
    }

    public boolean justConnected(Channel channel) {
        return clients.containsValue(channel);
    }

    public void addClient(Channel channel, ChatMessage msg) {
        clients.put(msg.content,channel);
    }


    public boolean hasConnected(Channel channel) {
        return clients.containsValue(channel);
    }

    public boolean hasConnected(String clientId) {
        return clients.containsKey(clientId);
    }

    public Channel getClient(String to) {
        return clients.get(to);
    }
}
