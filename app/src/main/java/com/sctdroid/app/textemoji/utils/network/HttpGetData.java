
package com.sctdroid.app.textemoji.utils.network;

import android.text.TextUtils;

/**
 * Http request class
 */
public class HttpGetData {
    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String ENCODING_GZIP = "gzip";
    private int m_retry = 3;

    /**
     * Call executeHttpGet() function to execute the HTTP get request with 3
     * times retry
     */
    public String executeHttpGetWithRetry(String url) {
        int count = 0;
        String result = null;
        while (count < m_retry) {
            count += 1;
            result = executeHttpGet(url);
            if (null != result) {
                break;
            }
        }
        return result;
    }

    /**
     * Execute the HTTP request by GET method, setting the timeout is 10000
     */
    public String executeHttpGet(String url) {
        String result = null;
        HttpRequest request = HttpRequest.get(url)
                .accept("application/json")
                .connectTimeout(3000)
                .readTimeout(15000);
        request.acceptGzipEncoding().uncompress(true);
        try {
            if (request.ok()) {
                result = request.body("utf-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    /**
     * Call executeHttpGet() function to execute the HTTP get request with 3
     * times retry
     *
     * return bytes
     */
    public byte[] executeHttpGetWithRetryBytes(String url) {
        int count = 0;
        byte[] result = null;
        while (count < m_retry) {
            count += 1;
            result = executeHttpGetBytes(url);
            if (null != result) {
                break;
            }
        }
        return result;
    }

    /**
     * Execute the HTTP request by GET method, setting the timeout is 10000
     *
     * return bytes
     */
    public byte[] executeHttpGetBytes(String url) {
        byte[] result = null;
        HttpRequest request = HttpRequest.get(url)
                .connectTimeout(3000)
                .readTimeout(15000);
        request.acceptGzipEncoding().uncompress(true);
        try {
            if (request.ok()) {
                result = request.bytes();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

    public static String requestGet(String url) {
        if (TextUtils.isEmpty(url))
            return null;
        HttpGetData httpdata = new HttpGetData();
        String content = httpdata.executeHttpGetWithRetry(url);
        return content;
    }

    public static byte[] requestGetBytes(String url) {
        if (TextUtils.isEmpty(url))
            return null;
        HttpGetData httpdata = new HttpGetData();
        byte[] content = httpdata.executeHttpGetWithRetryBytes(url);
        return content;
    }
}
