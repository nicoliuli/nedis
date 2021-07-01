package com.nedis.model;

public class RedisServerConfig {
    /**
     * redis ip
     */
    private String ip;

    /**
     * redis port
     */
    private Integer port;

    public RedisServerConfig(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public Integer getPort() {
        return port;
    }
}
