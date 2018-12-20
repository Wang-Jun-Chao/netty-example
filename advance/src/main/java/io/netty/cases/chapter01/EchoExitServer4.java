package io.netty.cases.chapter01;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author: wangjunchao(王俊超)
 * @date: 2018-12-18 09:59:56
 **/
public class EchoExitServer4 {

    static Logger logger = LoggerFactory.getLogger(EchoExitServer4.class.getName());

    public static void main(String[] args) throws Exception {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(new LoggingHandler(LogLevel.INFO));
                        }
                    });
            ChannelFuture f = b.bind(18080).sync();
            f.channel().closeFuture().addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    // 业务逻辑代码，此处省略

                    // (2) 在链路关闭吋再释放线程池和连接句柄
                    bossGroup.shutdownGracefully();
                    workerGroup.shutdownGracefully();
                    logger.warn(future.channel().toString() + "链路关闭");
                }
            });
        } finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
        }
    }
}
