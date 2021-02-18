package com.clientsrc.message;

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

    public static ChatMessage createLogin(String userId){
        return new ChatMessage(userId,"","",ChatMessageType.CLIENT_LOGIN,FpManager.getFp());
    }

    public static ChatMessage createLogout(){
        return new ChatMessage("","","",ChatMessageType.CLIENT_LOGOUT,FpManager.getFp());
    }

    public static ChatMessage createChatMessage(String content,String from,String to){
        return new ChatMessage(content,from,to,ChatMessageType.CLIENT_TO_CLIENT,FpManager.getFp());
    }
}
