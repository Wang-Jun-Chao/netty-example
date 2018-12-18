package io.netty.cases.chapter01;

import java.util.concurrent.TimeUnit;

/**
 * @author: wangjunchao(王俊超)
 * @date: 2018-12-18 09:50:37
 **/
public class Shutdown1 {

    public static void main(String[] args) throws Exception {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("ShutdownHook execute start...");
            System.out.println("Netty NioEventLoopGroup shutdownGracefully...");
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("ShutdownHook execute end...");
        }, ""));
        TimeUnit.SECONDS.sleep(7);
        System.exit(0);
    }
}
