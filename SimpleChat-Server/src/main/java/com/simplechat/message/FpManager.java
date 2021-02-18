package com.simplechat.message;

import java.util.UUID;

public class FpManager {
    public static String getFp(){
        return UUID.randomUUID().toString();
    }
}
