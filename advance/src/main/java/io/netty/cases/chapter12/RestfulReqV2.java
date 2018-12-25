package io.netty.cases.chapter12;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpVersion;

import java.util.Arrays;

/**
 * @author: wangjunchao(王俊超)
 * @date: 2018-12-19 11:00:08
 */
public class RestfulReqV2 {

    private HttpHeaders header;

    private HttpMethod method;

    private HttpVersion version;

    private byte[] body;

    public RestfulReqV2(byte[] body) {
        this.body = body;
    }


    public byte[] body() {
        return this.body;
    }

    public byte[] bodyCopy() {
        if (this.body != null) {
            return Arrays.copyOf(this.body, this.body.length);
        }
        return null;
    }
}
