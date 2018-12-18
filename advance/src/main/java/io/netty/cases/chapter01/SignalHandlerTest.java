package io.netty.cases.chapter01;

import sun.misc.Signal;

import java.util.concurrent.TimeUnit;

/**
 * @author: wangjunchao(王俊超)
 * @date: 2018-12-18 10:31:07
 **/
public class SignalHandlerTest {

    public static void main(String[] args) throws Exception {
        // 以windows操作系统为例
        Signal sig = new Signal("INT");
        Signal.handle(sig, (s) -> {
            System.out.println("Signal handle start...");
            try {
                TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

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

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.DAYS.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Daemon-T").start();
    }
}
