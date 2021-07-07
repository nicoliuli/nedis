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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class Client {
    private int port;
    private Channel channel;
    private List<Server> serverList;
    private static Logger logger = LoggerFactory.getLogger(Client.class);

    public Client(int port, List<Server> serverList) {
        this.port = port;
        this.serverList = serverList;
    }

    public void start() {

        EventLoopGroup bossGroup = new NioEventLoopGroup(5);
        EventLoopGroup workerGroup = new NioEventLoopGroup(6);
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                 //   .childOption(ChannelOption.SO_BACKLOG, 8 * 1024)
                    .childOption(ChannelOption.SO_SNDBUF,8 * 10240)
                    .childOption(ChannelOption.SO_RCVBUF,8 * 10240)
                    .childHandler(new RedisClientInitializer(serverList));

            ChannelFuture f = b.bind(port).sync().addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    if (future.isSuccess()) {
                       logger.info("client start");
                    }
                }
            });
            channel = f.channel();
            channel.closeFuture().sync().addListener(new GenericFutureListener<Future<? super Void>>() {
                @Override
                public void operationComplete(Future<? super Void> future) throws Exception {
                    logger.info("client close");
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }


}
