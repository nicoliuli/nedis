package com.nedis.config;

import com.nedis.model.RedisServerConfig;

import java.util.ArrayList;
import java.util.List;

public class Config {
    /**
     * client监听的端口
     */
    private Integer clientPort;

    /**
     * redis server的ip和端口
     */
    private List<RedisServerConfig> redisServerConfigList = new ArrayList<>();

    /**
     * 每个redis的连接数
     */
    private Integer serverChannel;

    public Config() {
    }



    public Integer getClientPort() {
        return clientPort;
    }

    public void setClientPort(Integer clientPort) {
        this.clientPort = clientPort;
    }

    public List<RedisServerConfig> getRedisServerConfigList() {
        return redisServerConfigList;
    }

    public void setRedisServerConfigList(List<RedisServerConfig> redisServerConfigList) {
        this.redisServerConfigList = redisServerConfigList;
    }

    public Integer getServerChannel() {
        return serverChannel;
    }

    public void setServerChannel(Integer serverChannel) {
        this.serverChannel = serverChannel;
    }
}
