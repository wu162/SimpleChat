package com.simplechat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 服务端配置类
 */
public class ServerConfig {

    private Logger logger = LoggerFactory.getLogger(ServerConfig.class);

    private String propFile = "simpleChat.properties";
    public int port = 8090;
    public int readTimeout = 240;
    public int writeTimeout = 60;
    public int resendTimeout= 10;
    public int retryCount=10;

    static class ServerConfigHolder {
        static ServerConfig instance = new ServerConfig();
    }

    private ServerConfig() {
        initConfig();
    }

    public static ServerConfig get() {
        return ServerConfigHolder.instance;
    }

    private void initConfig() {
        if (!new File(propFile).exists()) {
            logger.debug("Not found simpleChat.properties , use default settings");
            return;
        }
        Properties properties = new Properties();
        try (InputStream is = ServerConfig.class.getClassLoader().getResourceAsStream(propFile)) {
            properties.load(is);
            port = Integer.parseInt(properties.getProperty("server.port", String.valueOf(8090)));
            readTimeout = Integer.parseInt(properties.getProperty("server.readTimeout", String.valueOf(240)));
            writeTimeout = Integer.parseInt(properties.getProperty("server.writeTimeout", String.valueOf(60)));
            resendTimeout = Integer.parseInt(properties.getProperty("server.resendTimeout", String.valueOf(10)));
            retryCount = Integer.parseInt(properties.getProperty("server.retryCount", String.valueOf(10)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
