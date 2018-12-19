package io.netty.cases.chapter01;

import java.util.concurrent.TimeUnit;

/**
 * @author: wangjunchao(王俊超)
 * @date: 2018-12-19 11:00:08
 */
public class DaemonT2 {
    public static void main(String[] args)
            throws IllegalArgumentException, InterruptedException {
        long startTime = System.nanoTime();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.SECONDS.sleep(30);
                    System.out.println("NO-Daemon-T 运行30S退出");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "NO-Daemon-T");

        // 设置成非守护线程
        t.setDaemon(false);
        t.start();
        TimeUnit.SECONDS.sleep(15);
        System.out.println("main线程退出，程序执行" + (System.nanoTime() - startTime) / 1000 / 1000 / 1000 + " s");
    }
}
