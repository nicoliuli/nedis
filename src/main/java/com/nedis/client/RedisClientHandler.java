package com.nedis.client;

import com.nedis.common.CmdSet;
import com.nedis.common.Constants;
import com.nedis.model.CmdAndKey;
import com.nedis.server.Server;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.util.CharsetUtil;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

public class RedisClientHandler extends ChannelDuplexHandler {

    private static Logger logger = LoggerFactory.getLogger(RedisClientHandler.class);

    private Server [] servers;
    private int serverCount;


    public RedisClientHandler(Server [] servers) {
        this.servers = servers;
        serverCount = this.servers.length;
    }

    /**
     * 将redis的响应发送给redis-cli
     */
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        ctx.writeAndFlush(msg);
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf byteBuf = (ByteBuf) msg;
        String stringCmd = byteBuf.toString(CharsetUtil.UTF_8);
        Server server = null;
        int serverIdx = 0;

        // 解析命令，算出key等信息
        CmdAndKey cmdAndKey = resolveStringCmd(stringCmd);

        if (cmdAndKey == null || CmdSet.isOtherCmd(cmdAndKey.getCmd())) {
            serverIdx = new Random().nextInt(serverCount);
            server = servers[serverIdx];
        } else if (CmdSet.isNormorCmd(cmdAndKey.getCmd())) {
            String key = cmdAndKey.getKey();
            serverIdx = key.hashCode() % serverCount;
            server = servers[serverIdx];
        }
        // 兜底
        if (server == null) {
            serverIdx = new Random().nextInt(serverCount);
            server = servers[serverIdx];
        }

        // 发给server,把client的channel通过attr属性带过去
        Channel channel = server.pop();
        channel.attr(Constants.attributeKey).set(ctx.channel());
        channel.writeAndFlush(msg);
        server.free(channel);
    }

    /*
       规则：目的是为了解析出key
       如果key是monitor、info、keys等命令，则随机挑选出一个server发送
       如果是一般的key，则根据hash选择server发送
     */
    private CmdAndKey resolveStringCmd(String stringCmd) {
        stringCmd = stringCmd.toLowerCase();
     //   logger.info("stringCmd = {}", stringCmd.replace(Constants.ENTRY, ""));
        String[] cmdArr = stringCmd.split(Constants.ENTRY);
        String key = "";
        // info ==> *1$4info
        if (Constants.X1.equalsIgnoreCase(cmdArr[0])) {
            key = cmdArr[2];
            return new CmdAndKey(key, null);
        }

        // keys * ==> *2$4keys$1*
        if (stringCmd.startsWith("*2") && Constants.KEYS.equalsIgnoreCase(cmdArr[2])) {
            return new CmdAndKey(Constants.KEYS, null);
        }

        // 一般的key
        int index = 0;
        for (String cmd : cmdArr) {
            if (cmd.startsWith(Constants.$)) {
                return new CmdAndKey(cmdArr[index + 1], cmdArr[index + 3]);
            }
            index++;
        }
        return null;
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 这里如果是jedis关闭连接后，会抛异常
         System.err.print("exceptionCaught: ");
         cause.printStackTrace(System.err);
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // logger.info("connection client active: {}", ctx.channel().id());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // logger.info("connention client inactive: {}",ctx.channel().id());
    }
}
