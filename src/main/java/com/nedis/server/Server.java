package com.nedis.server;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Server {
    private String host;
    private int port;
    private Channel channel;
    private ConcurrentLinkedQueue<Channel> serverChannelPool = new ConcurrentLinkedQueue<>();
    private static Logger logger = LoggerFactory.getLogger(Server.class);


    public Server(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new RedisServerInitializer());

            ChannelFuture f = bootstrap.connect(host, port).sync().addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    logger.info("server start");
                }
            });
            channel = f.channel();
            serverChannelPool.add(channel);
            channel.closeFuture().addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    logger.info("server channel close");
                    // server与redis断开链接时，不能从serverChannel移除。会导路由key的错乱
                }
            });
        } finally {

        }
    }


    public Channel pop() {
        Channel channel = serverChannelPool.poll();
        return channel;
    }

    public void free(Channel channel) {
        serverChannelPool.add(channel);
    }



}
