package com.clientsrc.message;

public class ChatMessageType {
    public static final int CLIENT_TO_SERVER=100;
    public static final int CLIENT_TO_CLIENT=101;
    public static final int CLIENT_KEEP_ALIVE=102;
    public static final int CLIENT_RECEIVED_BACK=103;
    public static final int CLIENT_LOGIN=104;
    public static final int CLIENT_LOGOUT=105;

    public static final int SERVER_TO_CLIENT=200;
    public static final int SERVER_KEEP_ALIVE=201;
    public static final int SERVER_RECEIVED_BACK=202;
    public static final int SERVER_LOGIN_SUCCESS=203;
    public static final int SERVER_LOGOUT_SUCCESS=204;

    //这个类是工具类，禁止初始化
    private ChatMessageType(){}
}
