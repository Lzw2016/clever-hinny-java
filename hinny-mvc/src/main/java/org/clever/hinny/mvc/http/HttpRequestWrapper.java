package org.clever.hinny.mvc.http;

import org.springframework.util.Assert;

import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.security.Principal;
import java.util.*;

/**
 * 作者：lizw <br/>
 * 创建时间：2019/09/20 17:27 <br/>
 */
public class HttpRequestWrapper {
    protected HttpContext httpContext;
    private final HttpServletRequest delegate;

    // public HttpRequestWrapper(HttpContext httpContext) {
    //     Assert.notNull(httpContext, "参数httpContext不能为空");
    //     this.httpContext = httpContext;
    //     this.delegate = httpContext.request.delegate;
    // }

    protected HttpRequestWrapper(HttpServletRequest request) {
        Assert.notNull(request, "参数request不能为空");
        this.delegate = request;
    }

    /**
     * 原始HTTP请求对象
     */
    public HttpServletRequest originalRequest() {
        return delegate;
    }

    /**
     * 返回一个包含所有的绑定到请求的对象名称的字符串对象的集合
     */
    public List<String> getAttributeNames() {
        List<String> list = new ArrayList<>();
        Enumeration<String> enumeration = delegate.getAttributeNames();
        while (enumeration.hasMoreElements()) {
            list.add(enumeration.nextElement());
        }
        return list;
    }

    /**
     * 返回具有给定名字的 servlet container 的属性,或者当没有具有所给名字的属性时，返回一个空值
     */
    public Object getAttribute(String name) {
        return delegate.getAttribute(name);
    }

    /**
     * 存储一个请求消息中的属性
     */
    public void setAttribute(String name, Object value) {
        delegate.setAttribute(name, value);
    }

    /**
     * 从请求消息中删除一个属性
     */
    public void removeAttribute(String name) {
        delegate.removeAttribute(name);
    }

    /**
     * 返回用在请求信息的body编码的字符的名称
     */
    public String getCharacterEncoding() {
        return delegate.getCharacterEncoding();
    }

    /**
     * 返回请求体的用字节表示的长度，并被输入流改变为有效。如果长度未知，就返回-1
     */
    public int getContentLength() {
        return delegate.getContentLength();
    }

    /**
     * 返回请求体的 MIME类型，当长度未知是就返回一个空值
     */
    public String getContentType() {
        return delegate.getContentType();
    }

    /**
     * 用一个 ServletInputStream 重新得到二进制的请求消息体
     */
    public ServletInputStream getInputStream() throws IOException {
        return delegate.getInputStream();
    }

    /**
     * 用一个 BufferedReader 重新得到字符串数据的请求消息体
     */
    public BufferedReader getReader() throws IOException {
        return delegate.getReader();
    }

    /**
     * 返回 基于Accept-Language 的头部的客户端将接收内容的首选的场所
     */
    public Locale getLocale() {
        return delegate.getLocale();
    }

    /**
     * 返回一个地址对象的枚举变量，它以开始的首选地址的降序排列指明了基于 Accept-Language header 的客户端可接受的地点
     */
    public List<Locale> getLocales() {
        List<Locale> list = new ArrayList<>();
        Enumeration<Locale> enumeration = delegate.getLocales();
        while (enumeration.hasMoreElements()) {
            list.add(enumeration.nextElement());
        }
        return list;
    }

    /**
     * 以 protocol/majorVersion.minorVersion, 的格式返回请求所用协议的名称和版本。例如HTTP/1.1
     */
    public String getProtocol() {
        return delegate.getProtocol();
    }

    /**
     * 返回一个布尔值以指明是否这个请求使用了一个安全信道，如HTTPS
     */
    public boolean isSecure() {
        return delegate.isSecure();
    }

    /**
     * 返回一个作为位于给定路径的资源资源的封装器的 RequestDispatcher 对象
     */
    public RequestDispatcher getRequestDispatcher(String path) {
        return delegate.getRequestDispatcher(path);
    }

    public DispatcherType getDispatcherType() {
        return delegate.getDispatcherType();
    }

    /**
     * 返回用以作出请求消息的方案的名称，如 http, https, 或ftp等
     */
    public String getScheme() {
        return delegate.getScheme();
    }

    /**
     * 返回收到请求的服务器主机的名字
     */
    public String getServerName() {
        return delegate.getServerName();
    }

    /**
     * 返回收到请求的端口号
     */
    public int getServerPort() {
        return delegate.getServerPort();
    }

    /**
     * 返回一个请求参数的字符串值。若该参数不存在，则返回一个空值
     */
    public String getParameter(String name) {
        return delegate.getParameter(name);
    }

    /**
     * 返回一个包含了请求中的参数名字的字符串对象的枚举变量
     */
    public List<String> getParameterNames() {
        List<String> list = new ArrayList<>();
        Enumeration<String> enumeration = delegate.getParameterNames();
        while (enumeration.hasMoreElements()) {
            list.add(enumeration.nextElement());
        }
        return list;
    }

    /**
     * 返回一个包含所有的给定请求参数的值的字符串对象的向量。若该参数不存在，则返回一个空值
     */
    public String[] getParameterValues(String name) {
        String[] parameters = delegate.getParameterValues(name);
        if (parameters == null) {
            parameters = new String[0];
        }
        return parameters;
    }

    /**
     * 获取所有请求参数
     */
    public Map<String, String[]> getParameterMap() {
        return delegate.getParameterMap();
    }

    /**
     * 返回客户端发送请求的IP地址
     */
    public String getRemoteAddr() {
        return delegate.getRemoteAddr();
    }

    /**
     * 返回发送请求的客户端的完全合格的名称；或者如果客户端的名字没有确定则返回其IP地址
     */
    public String getRemoteHost() {
        return delegate.getRemoteHost();
    }

    /**
     * Remote Port
     */
    public int getRemotePort() {
        return delegate.getRemotePort();
    }

    /**
     * Local Addr
     */
    public String getLocalAddr() {
        return delegate.getLocalAddr();
    }

    /**
     * Local Name
     */
    public String getLocalName() {
        return delegate.getLocalName();
    }

    /**
     * Local Port
     */
    public int getLocalPort() {
        return delegate.getLocalPort();
    }

    /**
     * 返回用于保护servlet的认证方案的名字，例如， "BASIC" 或 "SSL,"。如果servlet没有被保护则返回一个空值
     */
    public String getAuthType() {
        return delegate.getAuthType();
    }

    /**
     * 如果用户已被授权认证，则返回用户作出请求的登录情况；否则就返回一个空值
     */
    public String getRemoteUser() {
        return delegate.getRemoteUser();
    }

    /**
     * 返回由客户机指定的会话ID
     */
    public String getRequestedSessionId() {
        return delegate.getRequestedSessionId();
    }

    /**
     * 返回关于该请求的当前会话。或者若该请求没有会话则就创建一个
     */
    public HttpSessionWrapper getSession(boolean create) {
        HttpSession session = delegate.getSession();
        final boolean exists = session != null;
        session = delegate.getSession(create);
        final boolean nowExists = session != null;
        if (exists != nowExists) {
            // 创建了 HttpSession - 需要刷新 httpContext.session - 值
            httpContext.session = new HttpSessionWrapper(session);
        }
        return httpContext.session;
    }

    /**
     * 返回有关本请求的当前HttpSession，或者若该请求没有会话，且“创建”属性为真，则就创建一个
     */
    public HttpSessionWrapper getSession() {
        return httpContext.session;
    }

    /**
     * 检查是否要求的会话ID已作为cookie来到
     */
    public boolean isRequestedSessionIdFromCookie() {
        return delegate.isRequestedSessionIdFromCookie();
    }

    /**
     * 检查是否要求的会话ID已作为请求URL的一部分到达
     */
    public boolean isRequestedSessionIdFromURL() {
        return delegate.isRequestedSessionIdFromURL();
    }

    /**
     * 检查是否要求的会话ID仍有效
     */
    public boolean isRequestedSessionIdValid() {
        return delegate.isRequestedSessionIdValid();
    }

    /**
     * 返回一个包含了当前被授权的用户名字的 java.security.Principal 对象
     */
    public Principal getUserPrincipal() {
        return delegate.getUserPrincipal();
    }

    /**
     * 返回一个布尔值以指明是否被授权的用户已被包含在指定的逻辑"role"中
     */
    public boolean isUserInRole(String role) {
        return delegate.isUserInRole(role);
    }

    /**
     * 返回指明请求context的请求URL的部分
     */
    public String getContextPath() {
        return delegate.getContextPath();
    }

    /**
     * 返回调用servlet的请求的URL部分<br />
     * 请求url path，如：/api/demo_1
     */
    public String getServletPath() {
        return delegate.getServletPath();
    }

    /**
     * 返回用以作出请求的HTTP方法的名称，例如 GET, POST,或 PUT等等
     */
    public String getMethod() {
        return delegate.getMethod();
    }

    /**
     * 返回该请求消息的URL中HTTP协议第一行里从协议名称到请求字符串的部分<br />
     * 请求url path，如：/api/demo_1
     */
    public String getRequestURI() {
        return delegate.getRequestURI();
    }

    /**
     * 请求url(protocol、host、path)，如：http://demo.msvc.top:18081/api/demo_1
     */
    public String getRequestURL() {
        return String.valueOf(delegate.getRequestURL());
    }

    /**
     * 返回有关当客户端作出请求时的URL的任何额外的路径信息
     */
    public String getPathInfo() {
        return delegate.getPathInfo();
    }

    /**
     * 返回在servlet名字之后查询字符串之前的任何额外的路径信息，并将其转换成一个真正的路径
     */
    public String getPathTranslated() {
        return delegate.getPathTranslated();
    }

    /**
     * 返回包含在请求URL中路径之后的查询字符串<br />
     * 查询字符串，如：a=123a&b=456b
     */
    public String getQueryString() {
        return delegate.getQueryString();
    }

    /**
     * 返回所有的本请求消息包含的头名字的集合
     */
    public List<String> getHeaderNames() {
        List<String> list = new ArrayList<>();
        Enumeration<String> enumeration = delegate.getHeaderNames();
        while (enumeration.hasMoreElements()) {
            list.add(enumeration.nextElement());
        }
        return list;
    }

    /**
     * 返回指定的作为字符串请求消息头的值
     */
    public String getHeader(String name) {
        return delegate.getHeader(name);
    }

    /**
     * 以一个字符串对象集合的形式，返回包含指定请求消息头的所有值
     */
    public List<String> getHeaders(String name) {
        List<String> list = new ArrayList<>();
        Enumeration<String> enumeration = delegate.getHeaders(name);
        while (enumeration.hasMoreElements()) {
            list.add(enumeration.nextElement());
        }
        return list;
    }

    /**
     * 返回一个指定的请求消息头的整数值
     */
    public int getIntHeader(String name) {
        return delegate.getIntHeader(name);
    }

    /**
     * 返回指定的请求消息头的值，返回值格式是描述日期对象的长型数值
     */
    public long getDateHeader(String name) {
        return delegate.getDateHeader(name);
    }

    /**
     * 返回包含所有的客户端随请求信息发送来的cookie对象的一个 array
     */
    public Cookie[] getCookies() {
        return delegate.getCookies();
    }

    public ServletContextWrapper getServletContext() {
        return httpContext.servletContext;
    }

//    /**
//     * Cookie的默认编码格式
//     */
//    private final static String DEFAULT_COOKIE_Encode = "UTF-8";
//    /**
//     * Http请求对象
//     */
//    private final HttpServletRequest request;
//    // @Getter
//    // private final ScriptObjectMirror wrapper = ScriptEngineUtils.newObject();
//    private final HashMap<String, Object> wrapper = this;
//
//    public HttpRequestWrapper(HttpServletRequest request) throws IOException {
//        this.request = request;
//        init();
//    }
//
//    /**
//     * <pre>
//     * method              -   请求方法        get
//     * protocol            -   请求协议        HTTP/1.1
//     * scheme              -   请求协议        http
//     * host                -   服务器主机名     demo.msvc.top
//     * hostname            -   服务器主机名     demo.msvc.top
//     * localName           -   服务器主机名     demo.msvc.top
//     * serverName          -   服务器主机名     demo.msvc.top
//     * ip                  -   服务器IP        192.168.33.121
//     * localAddr           -   服务器IP        192.168.33.121
//     * port                -   服务器端口号     18081
//     * serverPort          -   服务器端口号     18081
//     * localPort           -   服务器端口号     18081
//     * path                -   请求url path    /api/demo_1
//     * servletPath         -   请求url path    /api/demo_1
//     * uri                 -   请求url path    /api/demo_1
//     * queryString         -   查询字符串       a=123a&b=456b
//     * url                 -   请求url(protocol、host、path)                    http://demo.msvc.top:18081/api/demo_1
//     * href                -   请求完整字符串(protocol、host、path、querystring)  http://demo.msvc.top:18081/api/demo_1?a=123a&b=456b
//     * remoteAddr          -   客户端的IP地址    192.168.33.16
//     * remoteHost          -   客户端的主机名     192.168.33.16
//     * remotePort          -   客户端的端口号     36478
//     * remoteUser          -   客户端用户名
//     * charset             -   请求编码字符集
//     * characterEncoding   -   请求编码字符集
//     * type                -   请求Content-Type
//     * contentType         -   请求Content-Type
//     * length              -   请求Content-Length
//     * contentLength       -   请求Content-Length
//     * contextPath         -   Servlet ContextPath
//     * sessionId           -   Session Id
//     * parameters          -   请求参数(form、queryString)
//     * headers             -   请求头
//     * cookies             -   请求cookies
//     * content             -   请求body原始字符串
//     * body                -   请求body对象
//     * attributes          -   请求attributes
//     * // ---------------------------------------------------------------
//     * pathVariables
//     * getCookies(name, [options])
//     * </pre>
//     */
//    private void init() throws IOException {
//        String method = request.getMethod();                              // --
//        String url = request.getRequestURL().toString();                  // --
//        String uri = request.getRequestURI();                             // --
//        String contextPath = request.getContextPath();                    // --
//        String servletPath = request.getServletPath();                    // --
//        String pathInfo = request.getPathInfo();                          // --
//        String pathTranslated = request.getPathTranslated();              // --
//        String authType = request.getAuthType();                          // --
//        String contentType = request.getContentType();                    // --
//        String queryString = request.getQueryString();                    // --
//        String remoteUser = request.getRemoteUser();                      // --
//        String requestedSessionId = request.getRequestedSessionId();      // --
//        String characterEncoding = request.getCharacterEncoding();        // --
//        String localAddr = request.getLocalAddr();                        // --
//        String remoteAddr = request.getRemoteAddr();                      // --
//        String remoteHost = request.getRemoteHost();                      // --
//        String localName = request.getLocalName();                        // --
//        String protocol = request.getProtocol();                          // --
//        String scheme = request.getScheme();                              // --
//        String serverName = request.getServerName();                      // --
//        int serverPort = request.getServerPort();                         // --
//        int contentLength = request.getContentLength();                   // --
//        int remotePort = request.getRemotePort();
//        int localPort = request.getLocalPort();
//        // --------------------------------------------------------------------- 常用属性
//        wrapper.put("method", method);
//        wrapper.put("host", localName);
//        wrapper.put("hostname", localName);
//        wrapper.put("localName", localName);
//        wrapper.put("serverName", serverName);
//        wrapper.put("ip", localAddr);
//        wrapper.put("localAddr", localAddr);
//        wrapper.put("port", localPort);
//        wrapper.put("serverPort", serverPort);
//        wrapper.put("localPort", localPort);
//        wrapper.put("path", servletPath);
//        wrapper.put("servletPath", servletPath);
//        wrapper.put("uri", uri);
//        wrapper.put("protocol", protocol);
//        wrapper.put("scheme", scheme);
//        wrapper.put("queryString", queryString);
//        wrapper.put("url", url);
//        String href = String.format("%s?%s", url, queryString);
//        wrapper.put("href", href);
//        wrapper.put("remoteAddr", remoteAddr);
//        wrapper.put("remoteHost", remoteHost);
//        wrapper.put("remotePort", remotePort);
//        wrapper.put("remoteUser", remoteUser);
//        wrapper.put("charset", characterEncoding);
//        wrapper.put("characterEncoding", characterEncoding);
//        wrapper.put("type", contentType);
//        wrapper.put("contentType", contentType);
//        wrapper.put("length", contentLength);
//        wrapper.put("contentLength", contentLength);
//        wrapper.put("contextPath", contextPath);
//        if (request.getSession() != null) {
//            wrapper.put("sessionId", request.getSession().getId());
//        }
//        // --------------------------------------------------------------------- 扩展属性
//        wrapper.put("authType", authType);
//        wrapper.put("pathInfo", pathInfo);
//        wrapper.put("pathTranslated", pathTranslated);
//        wrapper.put("requestedSessionId", requestedSessionId);
//        // --------------------------------------------------------------------- 请求参数(form、queryString)
//        Map<String, String[]> parameterMap = request.getParameterMap();
//        ScriptObjectMirror parameters = ScriptEngineUtils.newObject();
//        parameterMap.forEach((name, values) -> {
//            if (values == null) {
//                parameters.put(name, null);
//                return;
//            }
//            if (values.length == 1) {
//                parameters.put(name, values[0]);
//            } else if (values.length >= 1) {
//                parameters.put(name, ObjectConvertUtils.Instance.javaToJSObject(values));
//            }
//        });
//        wrapper.put("parameters", parameters);
//        // --------------------------------------------------------------------- 请求headers
//        Enumeration<String> headerEnumeration = request.getHeaderNames();
//        ScriptObjectMirror headers = ScriptEngineUtils.newObject();
//        while (headerEnumeration.hasMoreElements()) {
//            String headerName = headerEnumeration.nextElement();
//            Enumeration<String> values = request.getHeaders(headerName);
//            if (values == null) {
//                continue;
//            }
//            List<String> valueList = new ArrayList<>(1);
//            while (values.hasMoreElements()) {
//                valueList.add(values.nextElement());
//            }
//            if (valueList.size() <= 0) {
//                headers.put(headerName, null);
//            } else if (valueList.size() == 1) {
//                headers.put(headerName, valueList.get(0));
//            } else {
//                headers.put(headerName, ObjectConvertUtils.Instance.javaToJSObject(valueList));
//            }
//        }
//        wrapper.put("headers", headers);
//        // --------------------------------------------------------------------- 请求cookies
//        ScriptObjectMirror cookies = ScriptEngineUtils.newObject();
//        Cookie[] cookieArray = request.getCookies();
//        if (cookieArray != null) {
//            for (Cookie cookie : cookieArray) {
//                String value;
//                try {
//                    value = URLDecoder.decode(cookie.getValue(), DEFAULT_COOKIE_Encode);
//                } catch (Throwable e) {
//                    log.error("Cookie的值解码失败", e);
//                    value = cookie.getValue();
//                }
//                cookies.put(cookie.getName(), value);
//            }
//        }
//        wrapper.put("cookies", cookies);
//        // --------------------------------------------------------------------- 请求body
//        String content = IOUtils.toString(request.getInputStream(), characterEncoding);
//        wrapper.put("content", content);
//        if (StringUtils.isNotBlank(content) && contentType.toLowerCase().contains("application/json")) {
//            try {
//                JsonWrapper jsonWrapper = new JsonWrapper(content);
//                wrapper.put("body", ObjectConvertUtils.Instance.javaToJSObject(jsonWrapper.getInnerMap()));
//            } catch (Throwable e) {
//                wrapper.put("body", content);
//            }
//        }
//        // --------------------------------------------------------------------- 请求attributes
//        Enumeration<String> attributesEnumeration = request.getAttributeNames();
//        ScriptObjectMirror attributes = ScriptEngineUtils.newObject();
//        while (attributesEnumeration.hasMoreElements()) {
//            String attributeName = attributesEnumeration.nextElement();
//            Object attributeValue = request.getAttribute(attributeName);
//            // attribute 对象存在死循环的值
//            // attributes.put(attributeName, ObjectConvertUtils.Instance.javaToJSObject(attributeValue));
//            attributes.put(attributeName, attributeValue);
//        }
//        wrapper.put("attributes", attributes);
//    }
}
