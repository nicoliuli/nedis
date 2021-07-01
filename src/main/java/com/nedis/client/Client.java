package com.nedis.client;

import com.nedis.server.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.List;

public class Client {
    private int port;
    private Channel channel;
    private List<Server> serverList;

    public Client(int port, List<Server> serverList) {
        this.port = port;
        this.serverList = serverList;
    }

    public void start() {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new RedisClientInitializer(serverList));

            ChannelFuture f = b.bind(port).sync().addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isSuccess()) {
                        System.out.println("client start");
                    }
                }
            });
            channel = f.channel();
            f.channel().closeFuture().sync().addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    System.out.println("client close");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.close();
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


}
