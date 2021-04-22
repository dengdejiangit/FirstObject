package com.example.administrator.testproject.http;


/*
 * Create  by  User:Administrator  Data:2019/5/5  WS
 *
 */

import okhttp3.MediaType;
import okhttp3.RequestBody;

public class HttpUtil {

    private HttpUtil() {
    }

    private static class HttpUtilHolder {
        private static final HttpUtil INSTANCE = new HttpUtil();
    }

    public static HttpUtil getInstance() {
        return HttpUtilHolder.INSTANCE;
    }

    public RequestBody getRequestBody(String bodyString) {
        return RequestBody.create(MediaType.parse("application/json;charset=utf-8"), bodyString);
    }
}
