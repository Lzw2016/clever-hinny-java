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
public abstract class HttpRequestScriptHandler2<E, T> implements HandlerInterceptor {
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
    protected abstract TupleTow<T, String> getScriptObject(HttpServletRequest request);

    /**
     * 执行 Script 对象的函数
     */
    protected abstract Object doHandle(TupleTow<T, String> handlerScript, HttpContext httpContext);

    /**
     * 借一个引擎实例
     */
    protected abstract E borrowEngineInstance();

    /**
     * 归还引擎实例(有借有还)
     */
    protected abstract void returnEngineInstance(E engineInstance);

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        // 1.判断请求是否支持 Script 处理
        if (!supportScript(request, response, handler)) {
            return true;
        }
        long startTime1;        // 开始查找脚本文件时间
        long startTime2 = -1;    // 开始借一个引擎实例时间
        long startTime3 = -1;    // 开始加载脚本对象时间
        long startTime4 = -1;    // 开始执行脚本时间
        long startTime5 = -1;    // 开始序列化返回值时间
        // 2.获取处理请求的 Script 文件全路径和执行函数名
        startTime1 = System.currentTimeMillis();
        final TupleTow<String, String> scriptInfo = getScriptFileFullPathUseCache(request);
        if (scriptInfo == null || StringUtils.isBlank(scriptInfo.getValue1()) || StringUtils.isBlank(scriptInfo.getValue2())) {
            final long tmp = System.currentTimeMillis() - startTime1;
            if (tmp > 0) {
                log.debug("Script Handler不存在 | 总耗时 {}ms", tmp);
            }
            return true;
        }
        E engineInstance = null;
        try {
            // 3.借一个引擎实例
            startTime2 = System.currentTimeMillis();
            engineInstance = borrowEngineInstance();
            // 4.获取 Script 文件对应的 Script 对象和执行函数名
            startTime3 = System.currentTimeMillis();
            final TupleTow<T, String> scriptHandler = getScriptObject(request);
            if (scriptHandler == null) {
                log.warn("获取Script Handler对象失败");
                return true;
            }
            // 5.执行 Script 对象的函数
            response.setHeader("use-http-request-js-handler", String.format("%s#%s", scriptInfo.getValue1(), scriptInfo.getValue2()));
            startTime4 = System.currentTimeMillis();
            final HttpContext httpContext = new HttpContext(request, response);
            Object res = doHandle(scriptHandler, httpContext);
            // 6.序列化返回数据
            startTime5 = System.currentTimeMillis();
            if (res != null && !response.isCommitted() && !httpContext.response.isFinish()) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().println(JacksonMapper.getInstance().toJson(res));
            }
        } finally {
            // 7.归还借得的引擎实例
            if (engineInstance != null) {
                returnEngineInstance(engineInstance);
            }
            final long endTime = System.currentTimeMillis();
            // 总耗时
            final long howLongSum = endTime - startTime1;
            // 查找脚本耗时
            final long howLong1 = startTime2 - startTime1;
            // 借一个引擎耗时
            final long howLong2 = startTime3 <= -1 ? -1 : startTime3 - startTime2;
            // 加载脚本耗时
            final long howLong3 = startTime4 <= -1 ? -1 : startTime4 - startTime3;
            // 执行脚本耗时
            final long howLong4 = startTime5 <= -1 ? -1 : startTime5 - startTime4;
            // 序列化耗时
            final long howLong5 = endTime - startTime5;
            // 8.请求处理完成 - 打印日志
            log.debug(
                    "使用Script处理请求 | [file={} handler={}] | [总]耗时 {}ms | 查找脚本 {}ms | 借引擎 {}ms | 加载脚本 {}ms | 执行脚本 {}ms | 序列化 {}ms",
                    scriptInfo.getValue1(),         // Script 文件全路径
                    scriptInfo.getValue2(),         // Script 函数名
                    howLongSum,
                    howLong1,
                    howLong2 <= -1 ? "-" : howLong2,
                    howLong3 <= -1 ? "-" : howLong3,
                    howLong4 <= -1 ? "-" : howLong4,
                    howLong5 <= -1 ? "-" : howLong5
            );
        }
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
