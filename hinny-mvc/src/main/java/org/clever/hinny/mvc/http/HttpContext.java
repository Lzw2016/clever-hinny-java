package org.clever.hinny.mvc.http;

import org.springframework.util.Assert;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/08/24 22:04 <br/>
 */
public class HttpContext {

    public final HttpRequestWrapper request;
    public final HttpResponseWrapper response;
    public final HttpSessionWrapper session;
    public final ServletContextWrapper servletContext;

    public HttpContext(HttpServletRequest request, HttpServletResponse response) {
        Assert.notNull(request, "参数request不能为空");
        Assert.notNull(response, "参数response不能为空");
        this.request = new HttpRequestWrapper(request);
        this.response = new HttpResponseWrapper(response);
        HttpSession httpSession = request.getSession();

        HttpSessionWrapper sessionWrapper = null;
        ServletContextWrapper servletContextWrapper = null;
        if (httpSession != null) {
            sessionWrapper = new HttpSessionWrapper(httpSession);
            ServletContext servletContext = httpSession.getServletContext();
            if (servletContext != null) {
                servletContextWrapper = new ServletContextWrapper(servletContext);
            }
        }
        this.session = sessionWrapper;
        this.servletContext = servletContextWrapper;
    }
}
