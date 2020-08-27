package org.clever.hinny.mvc;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
        String requestUri = request.getRequestURI();
        boolean supportUri = false;
        for (String suffix : Support_Suffix) {
            if (StringUtils.isBlank(suffix)) {
                supportUri = true;
                continue;
            }
            if (requestUri.endsWith(suffix)) {
                supportUri = true;
                requestUri = requestUri.substring(0, requestUri.length() - suffix.length());
                break;
            }
        }
        return true;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // 1.判断请求是否支持 Script 处理
        // 2.获取处理请求的 Script 文件全路径
        // 3.获取 Script 文件对应的 Script 对象和执行函数名
        // 4.执行 Script 对象的函数


//
//        // 获取JS文件全名称
//        final long startTime1 = System.currentTimeMillis();
//        final String scriptFileFullPath = getScriptFileFullPathUseCache(request);
//        if (StringUtils.isBlank(scriptFileFullPath)) {
//            final long tmp = System.currentTimeMillis() - startTime1;
//            if (tmp > 0) {
//                log.debug("跳过script处理 | 总耗时 {}ms", tmp);
//            }
//            return true;
//        }
//        // 获取js模块对象处理请求
//        final long startTime2 = System.currentTimeMillis();
//        final ScriptObjectMirror jsHandler = getScriptHandler(scriptFileFullPath, response, handler);
//        if (jsHandler == null) {
//            return true;
//        }
//        // 使用js代码处理请求
//        final long startTime3 = System.currentTimeMillis();
//        response.setHeader("use-http-request-js-handler", jsHandlerFileFullName);
//        final long startTime4 = doHandle(jsHandler, jsHandlerFileFullName, request, response);
//        // 请求处理完成 - 打印日志
//        long endTime = System.currentTimeMillis();
//        log.debug(
//                "使用js代码处理请求 | [{}] | [总]耗时 {}ms | [Js处理全过程]耗时 {}ms | [Js函数调用]耗时 {}ms | [返回值序列化]耗时 {}ms",
//                jsHandlerFileFullName,
//                endTime - startTime1,
//                endTime - startTime2,
//                endTime - startTime3,
//                endTime - startTime4
//        );
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
