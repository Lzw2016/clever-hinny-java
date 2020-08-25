package org.clever.hinny.mvc.http;

import org.springframework.util.Assert;

import javax.servlet.ServletContext;

/**
 * 作者： lzw<br/>
 * 创建时间：2019-09-27 21:43 <br/>
 */
public class ServletContextWrapper {
    private final ServletContext delegate;

    public ServletContextWrapper(HttpContext httpContext) {
        Assert.notNull(httpContext, "参数servletContext不能为空");
        this.delegate = httpContext.servletContext.delegate;
    }

    protected ServletContextWrapper(ServletContext servletContext) {
        Assert.notNull(servletContext, "参数servletContext不能为空");
        this.delegate = servletContext;
    }


    //    private final HashMap<String, Object> wrapper = this;
//
//    @Getter
//    private final CurrentUserWrapper currentUserWrapper;
//    @Getter
//    private final HttpRequestWrapper requestWrapper;
//    @Getter
//    private final HttpSessionWrapper sessionWrapper;
//    @Getter
//    private final HttpResponseWrapper responseWrapper;
//
//    public ServletContextWrapper(HttpServletRequest request, HttpServletResponse response, JacksonMapper jacksonMapper) throws IOException {
//        this.currentUserWrapper = new CurrentUserWrapper("lizw", "13260658831");
//        this.requestWrapper = new HttpRequestWrapper(request);
//        this.responseWrapper = new HttpResponseWrapper(response, jacksonMapper);
//        HttpSession session = request.getSession(false);
//        if (session == null) {
//            this.sessionWrapper = null;
//            this.servletContext = null;
//        } else {
//            this.sessionWrapper = new HttpSessionWrapper(session);
//            this.servletContext = session.getServletContext();
//        }
//        if (servletContext != null) {
//            init();
//        }
//    }
//
//    @SuppressWarnings("DuplicatedCode")
//    private void init() {
//        wrapper.put("currentUser", currentUserWrapper);
//        wrapper.put("req", requestWrapper);
//        wrapper.put("res", responseWrapper);
//        wrapper.put("session", sessionWrapper);
//        if (servletContext != null) {
//            // --------------------------------------------------------------------- ServletContext attributes
//            ScriptObjectMirror attributes = ScriptEngineUtils.newObject();
//            Enumeration<String> attributeNames = servletContext.getAttributeNames();
//            if (attributeNames != null) {
//                while (attributeNames.hasMoreElements()) {
//                    String attributeName = attributeNames.nextElement();
//                    Object attributeValue = servletContext.getAttribute(attributeName);
//                    attributes.put(attributeName, attributeValue);
//                }
//            }
//            wrapper.put("attributes", attributes);
//        }
//    }
//
//    public void setAttribute(String name, Object value) {
//        if (servletContext == null) {
//            return;
//        }
//        servletContext.setAttribute(name, value);
//    }
}
