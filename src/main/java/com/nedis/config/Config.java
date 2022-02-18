package com.nedis.config;

import com.nedis.model.RedisServerConfig;

import java.util.ArrayList;
import java.util.List;

public class Config {
    /**
     * client监听的端口
     */
    private int clientPort;

    /**
     * redis server的ip和端口
     */
    private List<RedisServerConfig> redisServerConfigList = new ArrayList<>();

    /**
     * 每个redis的连接数
     */
    private int serverChannel;

    public Config() {
    }



    public int getClientPort() {
        return clientPort;
    }

    public void setClientPort(int clientPort) {
        this.clientPort = clientPort;
    }

    public List<RedisServerConfig> getRedisServerConfigList() {
        return redisServerConfigList;
    }

    public void setRedisServerConfigList(List<RedisServerConfig> redisServerConfigList) {
        this.redisServerConfigList = redisServerConfigList;
    }

    public int getServerChannel() {
        return serverChannel;
    }

    public void setServerChannel(int serverChannel) {
        this.serverChannel = serverChannel;
    }
}
