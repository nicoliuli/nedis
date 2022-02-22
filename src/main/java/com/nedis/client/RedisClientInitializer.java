package com.nedis.client;

import com.nedis.server.Server;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.List;

public class RedisClientInitializer extends ChannelInitializer<Channel> {



    private Server [] servers;

    public RedisClientInitializer(Server [] servers) {
        this.servers = servers;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ch.pipeline().addLast(new RedisClientHandler(servers));
    }
}
