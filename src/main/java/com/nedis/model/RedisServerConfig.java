package com.nedis.model;

public class RedisServerConfig {
    /**
     * redis ip
     */
    private String ip;

    /**
     * redis port
     */
    private int port;

    public RedisServerConfig(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }
}
