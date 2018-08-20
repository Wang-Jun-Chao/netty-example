package wjc.netty.discard;

import io.netty.buffer.ByteBuf;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: wangjunchao(王俊超)
 * @Time: 2018-08-20 11:09
 **/
public class DiscardUtil {
    private static final Logger logger = LoggerFactory.getLogger(DiscardUtil.class);
    public static void printMessage(ByteBuf byteBuf, String message) {
        ByteBuf buf = byteBuf.duplicate();
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        System.out.println(message + new String(bytes));
    }
}
