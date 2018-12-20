package io.netty.cases.chapter12;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.util.concurrent.TimeUnit;

/**
 * @author: wangjunchao(王俊超)
 * @date: 2018-12-19 11:00:08
 */
public class MockEdgeService {

    public static void main(String[] args) throws Exception {
//        testHotMethod();
        testCopyHotMethod();
//        testReferenceHotMethod();
    }

    static void testHotMethod() throws Exception {
        ByteBuf buf = Unpooled.buffer(1024);
        for (int i = 0; i < 1024; i++)
            buf.writeByte(i);
        RestfulReq req = new RestfulReq(buf.array());
        while (true) {
            byte[] msgReq = req.body();
            TimeUnit.MICROSECONDS.sleep(1);
        }
    }

    static void testCopyHotMethod() throws Exception {
        ByteBuf buf = Unpooled.buffer(1024);
        for (int i = 0; i < 1024; i++)
            buf.writeByte(i);
        RestfulReq req = new RestfulReq(buf.array());
        while (true) {
            byte[] msgReq = req.body();
        }
    }

    static void testReferenceHotMethod() throws Exception {
        ByteBuf buf = Unpooled.buffer(1024);
        for (int i = 0; i < 1024; i++)
            buf.writeByte(i);
        RestfulReqV2 req = new RestfulReqV2(buf.array());
        while (true) {
            byte[] msgReq = req.body();
        }
    }
}
