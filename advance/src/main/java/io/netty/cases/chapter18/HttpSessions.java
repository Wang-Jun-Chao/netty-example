package io.netty.cases.chapter18;

import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author: wangjunchao(王俊超)
 * @date: 2018-12-19 11:00:08
 */
public class HttpSessions {

    public static Map<String, NioSocketChannel> channelMap = new ConcurrentHashMap<>();
}
