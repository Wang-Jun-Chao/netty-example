package io.netty.cases.chapter01;

import java.util.concurrent.TimeUnit;

/**
 * <pre>
 * ��ν�ػ��߳�( Daemon)���������ڳ����̨���̣߳�ͨ���ػ��߳�����
 * JVM�����ģ����ڸ����û��̻߳���JVM�������Ƚϵ��͵���GC�̡߳��û���������
 * ��Ҳ�������ó�Daemon�߳�(ͨ����Ҫ��������)����������߳�(main �߳�)������
 * ���̡߳�Daemon �߳���Java����Ķ����ǣ�����������ֻ��Daemon�߳����У�����,����˳���
 *
 * (1)������п���ͬʱ�ж���߳����У�ֻ�е����еķ��ػ��߳�(ͨ�������û���
 * ��)��������ʱ��������Ľ��̲Ż���������ܵ�ǰ���е��߳��ǲ���main�̡߳�
 *
 * (2) main�߳����н����������ʱ���е������߳�ȫ����Daemon�̣߳�JVM��ʹ
 * ��Щ�߳�ֹͣ��ͬʱ�˳������������ʱ�������е������߳��з��ػ��̣߳���ô�����
 * ���еķ��ػ��߳̽�����JVM�Ż��˳���
 * </pre>
 *
 * @author: wangjunchao(������)
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

        // ���ó��ػ�����
        t.setDaemon(true);
        t.start();
        TimeUnit.SECONDS.sleep(15);
        System.out.println("ϵͳ�˳�������ִ��" + (System.nanoTime() - startTime) / 1000 / 1000 / 1000 + " s");
    }
}
