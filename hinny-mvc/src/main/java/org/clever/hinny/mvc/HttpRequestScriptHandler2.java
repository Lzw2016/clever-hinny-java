package org.clever.hinny.mvc;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.utils.mapper.JacksonMapper;
import org.clever.hinny.mvc.http.HttpContext;
import org.clever.hinny.mvc.support.TupleTow;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/08/24 21:34 <br/>
 */
@Slf4j
public class HttpRequestScriptHandler2<T> implements HandlerInterceptor {
    /**
     * 没有js file映射时的数据前缀
     */
    private static final String No_FileFullPath_Mapping = "&&&???###";
    private static final long No_FileFullPath_Mapping_TimeOut = 1000 * 60 * 10;
    /**
     * url path --> file full path
     */
    private static final Cache<String, String> Path_FileFullPath_Cache = CacheBuilder.newBuilder().maximumSize(4096).initialCapacity(1024).build();
    /**
     * 请求支持的后缀，建议使用特殊的后缀表示使用动态js代码处理请求
     */
    private static final Set<String> Support_Suffix = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("", ".json", ".action")));

    /**
     * 判断请求是否支持 Script 处理
     */
    protected boolean supportScript(HttpServletRequest request, HttpServletResponse response, Object handler) {
        final String requestUri = request.getRequestURI();
        boolean support = false;
        for (String suffix : Support_Suffix) {
            if (StringUtils.isBlank(suffix)) {
                support = true;
                continue;
            }
            if (requestUri.endsWith(suffix)) {
                support = true;
                break;
            }
        }
        return support;
    }

    /**
     * 获取处理请求的 Script 文件全路径<br/>
     * {@code TupleTow<ScriptFileFullPath, MethodName>}
     */
    protected TupleTow<String, String> getScriptFileFullPathUseCache(HttpServletRequest request) {
        return null;
    }

    /**
     * 获取 Script 文件对应的 Script 对象和执行函数名
     */
    protected TupleTow<T, String> getScriptObject(HttpServletRequest request) {
        return null;
    }

    /**
     * 执行 Script 对象的函数
     */
    protected Object doHandle(TupleTow<T, String> handlerScript, HttpContext httpContext) {
        return null;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // 1.判断请求是否支持 Script 处理
        if (!supportScript(request, response, handler)) {
            return true;
        }
        // 2.获取处理请求的 Script 文件全路径
        final long startTime1 = System.currentTimeMillis();
        final TupleTow<String, String> scriptInfo = getScriptFileFullPathUseCache(request);
        if (scriptInfo == null || StringUtils.isBlank(scriptInfo.getValue1()) || StringUtils.isBlank(scriptInfo.getValue2())) {
            final long tmp = System.currentTimeMillis() - startTime1;
            if (tmp > 0) {
                log.debug("Script Handler不存在 | 总耗时 {}ms", tmp);
            }
            return true;
        }
        // 3.获取 Script 文件对应的 Script 对象和执行函数名
        final long startTime2 = System.currentTimeMillis();
        TupleTow<T, String> scriptHandler = getScriptObject(request);
        if (scriptHandler == null) {
            log.warn("获取Script Handler对象失败");
            return true;
        }
        // 4.执行 Script 对象的函数
        response.setHeader("use-http-request-js-handler", String.format("%s#%s", scriptInfo.getValue1(), scriptInfo.getValue2()));
        final long startTime3 = System.currentTimeMillis();
        final HttpContext httpContext = new HttpContext(request, response);
        Object res = null;
        try {
            res = doHandle(scriptHandler, httpContext);
        } catch (Exception e) {
            log.error("Script Handler执行失败", e);
        }
        // 5.序列化返回数据
        final long startTime4 = System.currentTimeMillis();
        if (res != null && !response.isCommitted() && !httpContext.response.isFinish()) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().println(JacksonMapper.getInstance().toJson(res));
        }
        long endTime = System.currentTimeMillis();
        // 6.请求处理完成 - 打印日志
        log.debug(
                "使用Script处理请求 | [file={} handler={}] | [总]耗时 {}ms | [处理]耗时 {}ms | [调用]耗时 {}ms | [序列化]耗时 {}ms",
                scriptInfo.getValue1(),
                scriptInfo.getValue2(),
                endTime - startTime1,
                endTime - startTime2,
                endTime - startTime3,
                endTime - startTime4
        );
        return false;
    }

//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
//        log.debug("=================================================== postHandle");
//    }
//
//    @Override
//    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
//        log.debug("=================================================== afterCompletion");
//    }
}
