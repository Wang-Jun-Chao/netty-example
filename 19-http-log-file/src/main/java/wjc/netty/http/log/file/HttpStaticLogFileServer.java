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
package wjc.netty.http.log.file;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.internal.SystemPropertyUtil;

public final class HttpStaticLogFileServer {

    private static final int    DEFAULT_PORT = 8080;
    private static final String DEFAULT_PATH = SystemPropertyUtil.get("user.dir");

    public static void main(String[] args) throws Exception {

        int port = DEFAULT_PORT;

        String rootPath = DEFAULT_PATH;
        if (args != null && args.length > 0) {
            rootPath = args[0];
            if (args.length > 1) {
                try {
                    port = Integer.parseInt(args[1]);
                }catch (Exception e) {
                    System.out.println("error argument, use default port " + DEFAULT_PORT);
                }
            }
        }

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new HttpStaticLogFileServerInitializer(rootPath));

            Channel ch = b.bind(port).sync().channel();

            System.err.println("Open your web browser and navigate to http://127.0.0.1:" + port + '/');

            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
