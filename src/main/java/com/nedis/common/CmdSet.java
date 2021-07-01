package com.nedis.common;

import java.util.HashSet;
import java.util.Set;

/**
 * 允许输入的命令，持续更新中
 */
public class CmdSet {
    /**
     * 普通的命令，如get set hgetall
     */
    private static Set<String> normalCmdSet = new HashSet<>();

    /**
     * 其他命令，如monitor keys info ping
     */
    private static Set<String> otherCmsSet = new HashSet<>();

    static {
        normalCmdSet.add("get");
        normalCmdSet.add("set");
        normalCmdSet.add("hgetall");
        normalCmdSet.add("hget");
        normalCmdSet.add("sadd");
        normalCmdSet.add("zadd");
        normalCmdSet.add("zrange");
        normalCmdSet.add("zscore");
        normalCmdSet.add("smembers");
        normalCmdSet.add("type");

        otherCmsSet.add("info");
        otherCmsSet.add("keys");
        otherCmsSet.add("ping");
        otherCmsSet.add("monitor");
        otherCmsSet.add("command");
    }

    public static boolean isNormorCmd(String cmd) {
        return normalCmdSet.contains(cmd.toLowerCase());
    }

    public static boolean isOtherCmd(String cmd) {
        return otherCmsSet.contains(cmd.toLowerCase());
    }
}
