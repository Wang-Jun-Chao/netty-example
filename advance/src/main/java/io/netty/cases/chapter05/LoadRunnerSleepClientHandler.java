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

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <pre>
 * 并非只有高并发场景才会导致消息积压，在一些异常场景下，尽管系统流量不大，
 * 但仍然可能导致消息积压，可能的场景如下。
 * (1) 网络瓶颈，当发送速度超过网络链接处理能力，会导致发送队列积压。
 * (2) 当对端读取速度小于已方发送速度，导致自身TCP发送缓冲区满，频繁发生 write
 *      0 字节时，待发送消息会在Netty发送队列中排队。
 * 当出现大量排队时，很容易导致Netty的直接内存泄漏。对案例中的代码做改造，模
 * 拟直接内存泄漏，思路如下:服务端在消息接收处Debug，模拟服务端处理慢，不读取网
 * 络消息;客户端每1ms发送一条消息，由于服务端不读取网络消息，会导致客户端的发送
 * 队列积压。
 * </pre>

 *
 * @author: wangjunchao(王俊超)
 * @date: 2018-12-19 11:00:08
 */
public class LoadRunnerSleepClientHandler extends ChannelInboundHandlerAdapter {

    static final int SIZE = Integer.parseInt(System.getProperty("size", "10240"));
    private final ByteBuf firstMessage;
    Runnable loadRunner;
    AtomicLong sendSum = new AtomicLong(0);
    Runnable profileMonitor;

    /**
     * Creates a client-side handler.
     */
    public LoadRunnerSleepClientHandler() {
        firstMessage = Unpooled.buffer(SIZE);
        for (int i = 0; i < firstMessage.capacity(); i++) {
            firstMessage.writeByte((byte) i);
        }
    }

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        loadRunner = new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ByteBuf msg = null;
                while (true) {
                    byte[] body = new byte[SIZE];
                    msg = Unpooled.wrappedBuffer(body);
                    ctx.writeAndFlush(msg);
                    try {
                        TimeUnit.MILLISECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
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
