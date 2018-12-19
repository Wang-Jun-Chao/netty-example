package io.netty.cases.chapter03;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;

/**
 * @author: wangjunchao(王俊超)
 * @date: 2018-12-19 11:00:08
 */
public class PoolByteBufPerformanceTest {

    public static void main(String[] args) {
        unPoolTest();
        poolTest();
    }

    private static void unPoolTest() {
        long beginTime = System.currentTimeMillis();
        ByteBuf buf = null;
        int maxTimes = 10000000;
        for (int i = 0; i < maxTimes; i++) {
            buf = Unpooled.buffer(10 * 1024);
            buf.release();
        }
        System.out.println("Execute " + maxTimes + " times cost time : "
                + (System.currentTimeMillis() - beginTime));
    }

    private static void poolTest() {
        PooledByteBufAllocator allocator = new PooledByteBufAllocator(false);
        long beginTime = System.currentTimeMillis();
        ByteBuf buf = null;
        int maxTimes = 10000000;
        for (int i = 0; i < maxTimes; i++) {
            buf = allocator.heapBuffer(10 * 1024);
            buf.release();
        }
        System.out.println("Execute " + maxTimes + " times cost time : "
                + (System.currentTimeMillis() - beginTime));
    }
}
