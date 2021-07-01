package com.nedis.server;

import com.nedis.common.Constants;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class RedisServerHandler extends ChannelDuplexHandler {


    /**
     * 将redis-cli的数据发给redis
     */
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        ctx.writeAndFlush(msg);
    }


    /**
     * 接收redis的响应，并发送给redis-cli
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 获取client的channel，把数据写回给redis-cli
        Channel clientChannel = ctx.pipeline().channel().attr(Constants.attributeKey).get();
        clientChannel.writeAndFlush(msg);
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        System.err.print("exceptionCaught: ");
        cause.printStackTrace(System.err);
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("a server active:" + ctx.channel().id());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("a server inactive:" + ctx.channel().id());
    }
}
