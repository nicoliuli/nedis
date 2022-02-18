package com.nedis.server;

import com.nedis.common.Constants;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RedisServerHandler extends ChannelDuplexHandler {

    private static Logger logger = LoggerFactory.getLogger(RedisServerHandler.class);
    /**
     * 将redis-cli的数据发给redis
     */
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        //logger.info("server write channelId = {},{}",ctx.channel().id(),msg.toString());
        ctx.writeAndFlush(msg);
    }


    /**
     * 接收redis的响应，并发送给redis-cli
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 获取client的channel，把数据写回给redis-cli
        Channel clientChannel = ctx.pipeline().channel().attr(Constants.attributeKey).get();
        // logger.info("server read channelId = {},{}",ctx.channel().id(),msg.toString());
        clientChannel.writeAndFlush(msg);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("exceptionCaught: ",cause);
        cause.printStackTrace(System.err);
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        logger.info("a server active: {}",ctx.channel().id());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        logger.info("a server inactive: {}" ,ctx.channel().id());
    }
}
