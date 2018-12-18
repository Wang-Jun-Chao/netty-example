package io.netty.cases.chapter01;

import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * 所谓守护线程( Daemon)就是运行在程序后台的线程，通常守护线程是由
 * JVM创建的，用于辅助用户线程或者JVM工作，比较典型的如GC线程。用户创建的线
 * 程也可以设置成Daemon线程(通常需要谨慎设置)，程序的主线程(main 线程)不是守
 * 护线程。Daemon 线程在Java里面的定义是，如果虚拟机中只有Daemon线程运行，则虚,拟机退出。
 *
 * (1)虚拟机中可能同时有多个线程运行，只有当所有的非守护线程(通常都是用户线
 * 程)都结束的时候，虚拟机的进程才会结束，不管当前运行的线程是不是main线程。
 *
 * (2) main线程运行结束，如果此时运行的其他线程全部是Daemon线程，JVM会使
 * 这些线程停止，同时退出。但是如果此时正在运行的其他线程有非守护线程，那么必须等
 * 所有的非守护线程结束，JVM才会退出。
 * </pre>
 *
 * @author: wangjunchao(王俊超)
 * @date: 2018-12-18 09:26:10
 **/
public class DaemonT1 {
    public static void main(String[] args)
            throws IllegalArgumentException, InterruptedException {
        long startTime = System.nanoTime();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    TimeUnit.DAYS.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Daemon-T");

        // 设置成守护进程
        t.setDaemon(true);
        t.start();
        TimeUnit.SECONDS.sleep(15);
        System.out.println("系统退出，程序执行" + (System.nanoTime() - startTime) / 1000 / 1000 / 1000 + " s");
    }
}
