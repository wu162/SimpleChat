package com.clientsrc;

public class ClientConfig {
    public static String serverIP=null;
    public static int serverPort =-1;
    public static int readTimeout = 240;
    public static int writeTimeout = 60;
    public static int resendTimeout= 10;
    public static int retryCount=10;
    public static int reconnectInterval=1;
    public static int reconnectCount=5;
}
