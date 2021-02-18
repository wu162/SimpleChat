package com.simplechat;

import io.netty.util.AttributeKey;

public class Constants {
    public static final AttributeKey<String> USER_ID_ATTR = AttributeKey.newInstance("userId");
    public static final AttributeKey<ChatLauncher> CHAT_LAUNCHER = AttributeKey.newInstance("chatLauncher");
}
