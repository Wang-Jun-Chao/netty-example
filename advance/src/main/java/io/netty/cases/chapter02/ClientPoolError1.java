/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package io.netty.cases.chapter02;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;

/**
 * <pre>
 * 需要指出的是，Bootstrap 不是线程安全的，因此在多个线程中并发操作 Bootstrap 是
 * 一件比较危险的事情，Bootstrap 是 I/O 操作工具类，它自身的逻辑处理非常简单，真正的
 * I/O 操作都是由 EventLoop 线程负责的，所以通常多线程操作同一个 Bootstrap 实例也是没
 * 有意义的，而且容易出错
 * </pre>
 *
 * @author: wangjunchao(王俊超)
 * @date: 2018-12-18 13:15:30
 **/
public final class ClientPoolError1 {

    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int    PORT = Integer.parseInt(System.getProperty("port", "18081"));

    public static void main(String[] args) throws Exception {
//        TimeUnit.SECONDS.sleep(30);
        initClientPool(100);
    }

    static void initClientPool(int poolSize) throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline p = ch.pipeline();
                        p.addLast(new LoggingHandler());
                    }
                });
        // 在同一个 Bootstrap 中连续创建多个客户端连接，需要注意的是，EventLoopGroup 是
        // 共享的，也就是说这些连接共用同一个NIO线程组EventLoopGroup, 当某个链路发生异
        // 常或者关闭时，只需要关闭并释放 Channel 本身即可，不能同时销毁 Channel 所使用的
        // NioEventLoop 和所在的线程组 EventLoopGroup, 例如下面的代码就是错误的:
        for (int i = 0; i < poolSize; i++) {
            ChannelFuture f = b.connect(HOST, PORT).sync();
            f.channel().closeFuture().addListener((r) -> {
                group.shutdownGracefully();
            });
        }
    }
}
