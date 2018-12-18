package io.netty.cases.chapter01;

import java.util.concurrent.TimeUnit;

/**
 * Created by ���ַ� on 2018/8/3.
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
                    System.out.println("NO-Daemon-T ����30S�˳�");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "NO-Daemon-T");

        // ���óɷ��ػ��߳�
        t.setDaemon(false);
        t.start();
        TimeUnit.SECONDS.sleep(15);
        System.out.println("main�߳��˳�������ִ��" + (System.nanoTime() - startTime) / 1000 / 1000 / 1000 + " s");
    }
}
