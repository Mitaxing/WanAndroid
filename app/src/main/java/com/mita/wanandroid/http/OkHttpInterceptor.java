package com.mita.wanandroid.http;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * @className: OkHttpInterceptor
 * @classDescription: Http拦截器
 * @author: leibing
 * @createTime: 2016/08/30
 */
public class OkHttpInterceptor implements Interceptor {

    // 日志标识
    private final static String TAG = "JkRequest@OkHttpInterceptor";
    // utf8
    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        //created by yuzhenhong,2017/5/17，增加头
        Request.Builder requestBuilder = request.newBuilder()
                //Basic Authentication,也可用于token验证,OAuth验证
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .method(request.method(), request.body());
        Request request1 = requestBuilder.build();

        // 获得Connection，内部有route、socket、handshake、protocol方法
        Connection connection = chain.connection();
        // 如果Connection为null，返回HTTP_1_1，否则返回connection.protocol()
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        // 比如: --> POST http://121.40.227.8:8088/api http/1.1
        String requestStartMessage = "--> " + request.method() + ' ' + request.url() +' '+protocol;
        System.out.println("ccccccccccccc "+requestStartMessage);
        // 打印 NavigationBarAndSearchTitleResponse
        Response response;
        try {
            response = chain.proceed(request1);
        } catch (Exception e) {
            throw e;
        }
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();

        if (bodyEncoded(response.headers())) {

        } else {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();
            Charset charset = UTF8;
            if (contentLength != 0) {
                // 获取Response的body的字符串 并打印
            }
        }
        return response;
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }
}
