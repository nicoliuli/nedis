package com.nedis;


import com.nedis.client.Client;
import com.nedis.config.ConfigUtil;
import com.nedis.model.RedisServerConfig;
import com.nedis.server.Server;

import java.util.ArrayList;
import java.util.List;

public class Bootstrap {
    public static void main(String[] args) throws Exception {
        // 启动server,连接redis
        List<RedisServerConfig> redisServerConfigList = ConfigUtil.config.getRedisServerConfigList();
        List<Server> serverList = new ArrayList<>();
        for (RedisServerConfig redisServerConfig : redisServerConfigList) {
            Server aServer = new Server(redisServerConfig.getIp(), redisServerConfig.getPort());
            serverList.add(aServer);
            Integer serverChannel = ConfigUtil.config.getServerChannel();
            for (int i = 0; i < serverChannel; i++) {
                aServer.start();
            }

        }

        // 启动client，连接netty-server，和redis-cli
        Client client = new Client(ConfigUtil.config.getClientPort(), serverList);
        client.start();

        System.out.println("bb");
        // 添加hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("aaaaa");
            for (Server server : serverList) {
                server.closeChannel();
            }
        }));

    }
}
