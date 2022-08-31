package com.touchbiz.common.utils.web;

import com.touchbiz.common.utils.tools.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.util.CollectionUtils;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.UnsupportedEncodingException;
import java.net.ProxySelector;
import java.net.URLEncoder;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.codec.CharEncoding.UTF_8;


/**
 * Http请求工具类
 *
 * @Author: Steven Jiang(mrjiangyan@hotmail.com)
 * @Date: 2018/9/28 12：00
 */
@Slf4j
public final class HttpClientUtils {

    public static final int DEFAULT_SO_TIMEOUT = 10000;
    public static final int DEFAULT_CONN_TIMEOUT = 2000;
    private static final CloseableHttpClient HTTP_CLIENT;

    @Data
    @AllArgsConstructor
    public static class HttpResponse{
        private String response;

        private List<KeyValue> hedares;

        @Data
        @AllArgsConstructor
        public static class KeyValue{
            @Getter
            private String key;

            @Getter
            private String value;
        }
    }
    static {
        int maxTotalConnection = 1024;
        int defaultMaxPerRoute = 50;

        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };
        SSLContext sc = null;
        try {
            sc = SSLContext.getInstance("SSLv3");
            sc.init(null, new TrustManager[]{trustManager}, null);
        } catch (Exception e) {
            log.error("ssl init error", e);
        }
        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https", new SSLConnectionSocketFactory(sc)).build();
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        poolingHttpClientConnectionManager.setMaxTotal(maxTotalConnection);
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(defaultMaxPerRoute);
        SystemDefaultRoutePlanner systemDefaultRoutePlanner = new SystemDefaultRoutePlanner(ProxySelector.getDefault());
        HTTP_CLIENT = HttpClients.custom().setRoutePlanner(systemDefaultRoutePlanner).setConnectionManager(poolingHttpClientConnectionManager).build();
    }

    private HttpClientUtils() {
    }

    public static HttpResponse get(String url, Map<String, Object> headers, Map params) {
        return get(url, headers, params, DEFAULT_CONN_TIMEOUT, DEFAULT_SO_TIMEOUT);
    }


    /**
     * @param url
     * @param params
     * @param connectTimeout
     * @param readTimeout
     * @return
     */
    public static HttpResponse get(String url, Map<String, Object> headers, Map params, int connectTimeout, int readTimeout) {
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeout).setConnectTimeout(connectTimeout).setConnectionRequestTimeout(connectTimeout).build();
        String requestUrl;
        try {
            requestUrl = buildRequestUrl(url, params);
        } catch (UnsupportedEncodingException e) {
            log.error("encode http get params error, params is " + params, e);
            return null;
        }
        HttpGet httpGet = new HttpGet(requestUrl);
        httpGet.setConfig(requestConfig);

        return doRequest(httpGet, headers);
    }

    public static String buildRequestUrl(String url, Map<Object, Object> params) throws UnsupportedEncodingException {
        if (CollectionUtils.isEmpty(params)) {
            return url;
        }
        StringBuilder requestUrl = new StringBuilder();
        requestUrl.append(url);
        int i = 0;
        for (Map.Entry entry : params.entrySet()) {
            if (i == 0) {
                requestUrl.append("?");
            }
            requestUrl.append(entry.getKey());
            requestUrl.append("=");
            Object value = entry.getValue();
            String valueStr;
            if (value instanceof Object[]) {
                Object[] array = (Object[]) value;
                if (array.length > 0) {
                    valueStr = String.valueOf(array[0]);
                } else {
                    valueStr = "";
                }
            } else {
                valueStr = String.valueOf(value);
            }
            requestUrl.append(URLEncoder.encode(valueStr, UTF_8));
            requestUrl.append("&");
            i++;
        }
        requestUrl.deleteCharAt(requestUrl.length() - 1);
        return requestUrl.toString();
    }

    /**
     * @param requestUrl
     * @param params
     * @param connectTimeout
     * @param readTimeout
     * @return
     */
    public static HttpResponse post(String requestUrl, Map<String, Object> headers, Map<String, Object> params, int connectTimeout, int readTimeout, boolean isJson) {

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeout).setConnectTimeout(connectTimeout).setConnectionRequestTimeout(connectTimeout).build();
        HttpPost httpPost = new HttpPost(requestUrl);
        httpPost.setConfig(requestConfig);
        buildPostParams(httpPost, params, isJson);
        return doRequest(httpPost, headers);
    }

    public static HttpResponse post(String requestUrl, Map<String, Object> headers, Map<String, Object> params) {
        return post(requestUrl, headers, params, DEFAULT_CONN_TIMEOUT, DEFAULT_SO_TIMEOUT, true);
    }

    public static HttpResponse postFormData(String requestUrl, Map<String, Object> headers, Map<String, Object> params) {
        return post(requestUrl, headers, params, DEFAULT_CONN_TIMEOUT, DEFAULT_SO_TIMEOUT, false);
    }

    public static HttpResponse post(String requestUrl, Map<String, Object> headers) {
        return post(requestUrl, headers, null);
    }


    /**
     * @param httpPost
     * @param params   参数内部会转成json格式，最外层是表单post
     */
    private static void buildPostParams(HttpPost httpPost, Map<String, Object> params, boolean json) {
        if (!CollectionUtils.isEmpty(params)) {
            List<NameValuePair> nameValuePairs = new ArrayList<>();
            try {
                for (String key : params.keySet()) {
                    if (json) {
                        nameValuePairs.add(new BasicNameValuePair(key, JsonUtils.toJson(params.get(key))));
                    } else {
                        nameValuePairs.add(new BasicNameValuePair(key, params.get(key) == null ? null : params.get(key).toString()));
                    }

                }
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, UTF_8));
            } catch (UnsupportedEncodingException e) {
                log.error("HttpClientUtils.buildPostParams error, params = " + params, e);
            }
        }

    }

    /**
     * json格式提交
     *
     * @param requestUrl
     * @param headers
     * @param connectTimeout
     * @param readTimeout
     * @return
     */
    public static String postByJson(String requestUrl, Map<String, Object> headers, String json, int connectTimeout, int readTimeout) {
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(readTimeout).setConnectTimeout(connectTimeout).setConnectionRequestTimeout(connectTimeout).build();
        String responseString;
        HttpPost httpPost = new HttpPost(requestUrl);
        httpPost.setConfig(requestConfig);
        StringEntity s = new StringEntity(json, UTF_8);
        httpPost.setEntity(s);
        httpPost.setHeader("Content-Type", "application/json;CHARSET=utf-8");
        var response = doRequest(httpPost, headers);
        return response.getResponse();
    }

    public static String postByJson(String requestUrl, Map<String, Object> headers, String json) {
        return postByJson(requestUrl, headers, json, DEFAULT_CONN_TIMEOUT, DEFAULT_SO_TIMEOUT);
    }

    public static String postByJson(String requestUrl, String json) {
        return postByJson(requestUrl, null, json);
    }


    public static HttpResponse doRequest(HttpRequestBase httpRequestBase, Map<String, Object> headers) {

        String responseString = null;
        List<HttpResponse.KeyValue> list = null;
        try {
            //设置自定义header
            if (headers != null) {
                for (Map.Entry entry : headers.entrySet()) {
                    if (entry.getKey() != null && entry.getValue() != null) {
                        httpRequestBase.setHeader(entry.getKey().toString(), entry.getValue().toString());
                    }
                }
            }
            try (CloseableHttpResponse response = HTTP_CLIENT.execute(httpRequestBase)) {
                HttpEntity entity = response.getEntity();
                list = new ArrayList<>();
                for(int i=0;i<response.getAllHeaders().length;i++){
                    var header = response.getAllHeaders()[i];
                    list.add(new HttpResponse.KeyValue(header.getName(),header.getValue()));

                }
                try {
                    if (entity != null) {
                        responseString = EntityUtils.toString(entity, UTF_8);
                    }
                } finally {
                    if (entity != null) {
                        entity.getContent().close();
                    }
                }
            } catch (Exception e) {
                log.error(String.format("[HttpClientUtils.doRequest] get response error, url:%s", httpRequestBase.getURI()), e);
                responseString = "";
            }
        } catch (Exception e) {
            log.error(String.format("[HttpClientUtils.doRequest] invoke get error, url:%s", httpRequestBase.getURI()), e);
            responseString = "";
        } finally {
            httpRequestBase.releaseConnection();
        }
        return new HttpResponse(responseString,list);

    }
}

