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
     * @param name  名称
     * @param value 值
     */
    public void setRooPathCookie(HttpServletResponse response, String name, String value) {
        org.clever.common.utils.CookieUtils.setRooPathCookie(response, name, value);
    }

    /**
     * 设置Cookie
     *
     * @param name  名称
     * @param value 值
     */
    public void setCookie(HttpServletResponse response, String name, String value) {
        org.clever.common.utils.CookieUtils.setCookie(response, name, value);
    }

    /**
     * 获得指定Cookie的值
     *
     * @param request  请求对象
     * @param response 响应对象，设置移除Cookie时(isRemove=true),此对象不能传null
     * @param name     名字
     * @param isRemove 是否移除
     * @return Cookie值，不存在返回null
     */
    public String getCookie(HttpServletRequest request, HttpServletResponse response, String name, boolean isRemove) {
        return org.clever.common.utils.CookieUtils.getCookie(request, response, name, isRemove);
    }

    /**
     * 获得指定Cookie的值
     *
     * @param request 请求对象
     * @param name    名称
     * @return Cookie值
     */
    public String getCookie(HttpServletRequest request, String name) {
        return org.clever.common.utils.CookieUtils.getCookie(request, name);
    }

    /**
     * 获得指定Cookie的值，并删除
     *
     * @param request  请求对象
     * @param response 响应对象
     * @param name     名称
     * @return Cookie值
     */
    public String getCookieAndRemove(HttpServletRequest request, HttpServletResponse response, String name) {
        return org.clever.common.utils.CookieUtils.getCookieAndRemove(request, response, name);
    }
}
