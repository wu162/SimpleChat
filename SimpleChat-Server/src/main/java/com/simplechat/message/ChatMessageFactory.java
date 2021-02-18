package com.simplechat.message;

public class ChatMessageFactory {
    public static ChatMessage createServerReceived(String fp){
        return new ChatMessage(fp,"","",ChatMessageType.SERVER_RECEIVED_BACK,"");
    }

    public static ChatMessage createServerKeepAlive(){
        return new ChatMessage("","","",ChatMessageType.SERVER_KEEP_ALIVE,"");
    }

    public static ChatMessage createLoginSuccess(){
        return new ChatMessage("","","",ChatMessageType.SERVER_LOGIN_SUCCESS,"");
    }

    public static ChatMessage createLogoutSuccess(){
        return new ChatMessage("","","",ChatMessageType.SERVER_LOGOUT_SUCCESS,"");
    }
}
