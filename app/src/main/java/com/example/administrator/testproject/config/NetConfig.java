package com.example.administrator.testproject.config;

/**
 * @author WS
 */

public class NetConfig {
    private static String BASE_URL;

    public static String getBaseUrl(int apiType) {
        if (apiType == 1) {
            BASE_URL = "http://gank.io/api/";
        } else if (apiType == 2) {
            BASE_URL = "https://www.apiopen.top/";
        }
        return BASE_URL;
    }

    public static int NET_TYPE_GANK = 1;
    public static int NET_TYPE_OPEN_API = 2;
}
