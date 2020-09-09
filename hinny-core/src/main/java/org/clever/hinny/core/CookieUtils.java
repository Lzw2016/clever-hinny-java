package org.clever.hinny.core;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/07/28 22:34 <br/>
 */
public class CookieUtils {
    public static final CookieUtils Instance = new CookieUtils();

    private CookieUtils() {
    }

    /**
     * 设置Cookie
     *
     * @param response HTTP请求
     * @param path     Cookie的Path
     * @param name     名称
     * @param value    值
     * @param maxAge   Cookie生存时间，单位：秒。负数表示Cookie永不过期，0表示删除Cookie
     */
    public void setCookie(HttpServletResponse response, String path, String name, String value, int maxAge) {
        org.clever.common.utils.CookieUtils.setCookie(response, path, name, value, maxAge);
    }

    /**
     * 设置Cookie
     *
     * @param response HTTP请求
     * @param path     Cookie的Path
     * @param name     名称
     * @param value    值
     */
    public static void setCookie(HttpServletResponse response, String path, String name, String value) {
        org.clever.common.utils.CookieUtils.setCookie(response, path, name, value);
    }

    /**
     * 设置Cookie(当前路径)
     *
     * @param name  名称
     * @param value 值
     */
    public static void setCookieForCurrentPath(HttpServletResponse response, String name, String value) {
        org.clever.common.utils.CookieUtils.setCookieForCurrentPath(response, name, value);
    }

    /**
     * 设置Cookie(根路径)
     *
     * @param name  名称
     * @param value 值
     */
    public static void setCookieForRooPath(HttpServletResponse response, String name, String value) {
        org.clever.common.utils.CookieUtils.setCookieForRooPath(response, name, value);
    }

    /**
     * 获得指定Cookie的值
     *
     * @param request 请求对象
     * @param name    名字
     * @return Cookie值，不存在返回null
     */
    public static String getCookie(HttpServletRequest request, String name) {
        return org.clever.common.utils.CookieUtils.getCookie(request, name);
    }

    /**
     * 删除指定Cookie
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param name     名称
     * @param path     Cookie的Path
     */
    public static void delCookie(HttpServletRequest request, HttpServletResponse response, String name, String path) {
        org.clever.common.utils.CookieUtils.delCookie(request, response, name, path);
    }

    /**
     * 删除指定Cookie(当前路径)
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param name     名称
     */
    public static void delCookieForCurrentPath(HttpServletRequest request, HttpServletResponse response, String name) {
        org.clever.common.utils.CookieUtils.delCookieForCurrentPath(request, response, name);
    }

    /**
     * 删除指定Cookie(根路径)
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param name     名称
     */
    public static void delCookieForRooPath(HttpServletRequest request, HttpServletResponse response, String name) {
        org.clever.common.utils.CookieUtils.delCookieForRooPath(request, response, name);
    }
}
