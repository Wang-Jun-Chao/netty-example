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
package io.netty.cases.chapter05;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author: wangjunchao(王俊超)
 * @date: 2018-12-19 11:00:08
 */
public class LoadRunnerWaterClientHandler extends ChannelInboundHandlerAdapter {

    static final  int     SIZE = Integer.parseInt(System.getProperty("size", "256"));
    static        Logger  LOG  = LoggerFactory.getLogger(LoadRunnerWaterClientHandler.class.getName());
    private final ByteBuf firstMessage;
    Runnable loadRunner;
    AtomicLong sendSum = new AtomicLong(0);
    Runnable profileMonitor;

    /**
     * Creates a client-side handler.
     */
    public LoadRunnerWaterClientHandler() {
        firstMessage = Unpooled.buffer(SIZE);
        for (int i = 0; i < firstMessage.capacity(); i++) {
            firstMessage.writeByte((byte) i);
        }
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        // 为了防止在高并发场景下，由于服务端处理慢导致客户端消息积压，除了服务端做流
        // 控，客户端也需要做并发保护，防止自身发生消息积压。利用Netty提供的高低水位
        // 机制，可以实现客户端更精准的流控。

        ctx.channel().config().setWriteBufferHighWaterMark(10 * 1024 * 1024);
        loadRunner = new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ByteBuf msg;
                while (true) {
                    if (ctx.channel().isWritable()) {
                        msg = Unpooled.wrappedBuffer("Netty OOM Example".getBytes());
                        ctx.writeAndFlush(msg);
                    } else {
                        LOG.warn("The write queue is busy : " + ctx.channel().unsafe().outboundBuffer().nioBufferSize());
                    }
                }
            }
        };
        new Thread(loadRunner, "LoadRunner-Thread").start();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ReferenceCountUtil.release(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
