package org.clever.hinny.core;

import org.clever.common.utils.mapper.JacksonMapper;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/07/28 22:32 <br/>
 */
public class HttpUtils {
    public static final HttpUtils Instance = new HttpUtils();

    private final org.clever.common.utils.HttpUtils delegate;

    private HttpUtils() {
        delegate = org.clever.common.utils.HttpUtils.getInner();
    }

    public void test() {
    }

    // GET请求
    //----------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * 使用HTTP GET请求获取数据，支持参数，返回字符串
     *
     * @param url     请求url(非空)
     * @param params  Url Query Parameter(可选)
     * @param headers 请求头(可选)
     */
    public String getStr(final String url, final Map<String, String> params, final Map<String, String> headers) {
        return delegate.getStr(url, headers, params);
    }

    /**
     * 使用HTTP GET请求获取数据，支持参数，返回字符串
     *
     * @param url    请求url(非空)
     * @param params Url Query Parameter(可选)
     */
    public String getStr(final String url, Map<String, String> params) {
        return delegate.getStr(url, params);
    }

    /**
     * 使用HTTP GET请求获取数据，支持参数，返回字符串
     *
     * @param url 请求url(非空)
     */
    public String getStr(final String url) {
        return delegate.getStr(url);
    }

    /**
     * 使用HTTP GET请求获取数据，支持参数，返回字符串
     *
     * @param url     请求url(非空)
     * @param params  Url Query Parameter(可选)
     * @param headers 请求头(可选)
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getMap(final String url, final Map<String, String> params, final Map<String, String> headers) {
        String body = delegate.getStr(url, headers, params);
        return JacksonMapper.getInstance().fromJson(body, LinkedHashMap.class);
    }

    /**
     * 使用HTTP GET请求获取数据，支持参数，返回字符串
     *
     * @param url    请求url(非空)
     * @param params Url Query Parameter(可选)
     */
    public String getMap(final String url, Map<String, String> params) {
        return delegate.getStr(url, params);
    }

    /**
     * 使用HTTP GET请求获取数据，支持参数，返回字符串
     *
     * @param url 请求url(非空)
     */
    public String getMap(final String url) {
        return delegate.getStr(url);
    }
    // GET请求
    //----------------------------------------------------------------------------------------------------------------------------------------------


    // GET请求
    //----------------------------------------------------------------------------------------------------------------------------------------------

    // GET请求
    //----------------------------------------------------------------------------------------------------------------------------------------------

    // GET请求
    //----------------------------------------------------------------------------------------------------------------------------------------------
}
