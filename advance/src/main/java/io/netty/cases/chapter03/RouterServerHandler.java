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
package io.netty.cases.chapter03;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: wangjunchao(王俊超)
 * @date: 2018-12-19 10:59:06
 **/
public class RouterServerHandler extends ChannelInboundHandlerAdapter {
    static ExecutorService executorService = Executors.newSingleThreadExecutor();
    PooledByteBufAllocator allocator = new PooledByteBufAllocator(false);

    /**
     * 实现 channelRead 方法需要调用
     * {@link io.netty.util.ReferenceCountUtil#release(java.lang.Object)}
     * 进行内存释放，否则会出现内存泄漏
     * 不建议重写 channelRead 方法，使用 {@link io.netty.channel.SimpleChannelInboundHandler}类
     * 实现
     * {@link io.netty.channel.SimpleChannelInboundHandler#channelRead0(io.netty.channel.ChannelHandlerContext, java.lang.Object)}
     *
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf reqMsg = (ByteBuf) msg;
        byte[] body = new byte[reqMsg.readableBytes()];
        // 通过代码分析发现，请求 ByteBuf 被 Netty 框架申请后竟然没有被释放
        // 在业务代码中调用 ReferenceCountUtil 的 release 方法进行内存释放操作
        ReferenceCountUtil.release(reqMsg);
        executorService.execute(() -> {
            // 解析请求消息，做路由转发，代码省略...
            // 转发成功，返回响应给客户端
            ByteBuf respMsg = allocator.heapBuffer(body.length);
            // 作为示例，简化处理，将请求返回
            respMsg.writeBytes(body);
            ctx.writeAndFlush(respMsg);
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
