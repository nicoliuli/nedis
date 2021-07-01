package com.nedis.common;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;

public class Constants {
    public static final AttributeKey<Channel> attributeKey = AttributeKey.newInstance("client-channel");


    public static final String X1 = "*1";

    public static final String $ = "$";

    public static final String ENTRY = "\r\n";

    public static final String KEYS = "keys";
}
