//package org.clever.hinny.mvc;
//
//import com.google.common.cache.Cache;
//import com.google.common.cache.CacheBuilder;
//import jdk.nashorn.api.scripting.ScriptObjectMirror;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.apache.commons.lang3.math.NumberUtils;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * 作者：lizw <br/>
// * 创建时间：2020/08/24 21:34 <br/>
// */
//@Slf4j
//public class HttpRequestScriptHandler<T> implements HandlerInterceptor {
//    /**
//     * 没有js file映射时的数据前缀
//     */
//    private static final String No_FileFullPath_Mapping = "&&&???###";
//    private static final long No_FileFullPath_Mapping_TimeOut = 1000 * 60 * 10;
//    /**
//     * url path --> file full path
//     */
//    private static final Cache<String, String> Path_FileFullPath_Cache = CacheBuilder.newBuilder().maximumSize(4096).initialCapacity(1024).build();
//    /**
//     * 请求支持的后缀，建议使用特殊的后缀表示使用动态js代码处理请求
//     */
//    private static final Set<String> Support_Suffix = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("", ".json", ".action")));
////    private static final String Debug_Use_Js_Handler = "debugUseJsHandler";
//
////    /**
////     * 获取JS文件全名称 - 使用缓存
////     */
////    private String getScriptFileFullPathUseCache(final HttpServletRequest request) {
////
////
////        final String path = request.getServletPath();
////        String scriptFileFullPath = Path_FileFullPath_Cache.getIfPresent(path);
////        if (scriptFileFullPath != null && scriptFileFullPath.startsWith(No_FileFullPath_Mapping)) {
////            final long time = NumberUtils.toLong(scriptFileFullPath.substring(No_FileFullPath_Mapping.length()), -1);
////            if ((System.currentTimeMillis() - time) <= No_FileFullPath_Mapping_TimeOut) {
////                return null;
////            }
////            scriptFileFullPath = null;
////        }
////        if (StringUtils.isNotBlank(scriptFileFullPath) && jsCodeFileExists(scriptFileFullPath)) {
////            return scriptFileFullPath;
////        }
////        scriptFileFullPath = getJsHandlerFileFullName(request);
////        if (StringUtils.isNotBlank(scriptFileFullPath)) {
////            Path_FileFullPath_Cache.put(path, scriptFileFullPath);
////        } else {
////            Path_FileFullPath_Cache.put(path, No_FileFullPath_Mapping + System.currentTimeMillis());
////        }
////        return scriptFileFullPath;
////    }
//
////    /**
////     * 获取js模块对象处理请求
////     */
////    private ScriptObject getScriptHandler(final String scriptFileFullPath, final HttpServletResponse response, final Object handler) {
////        final ScriptObjectMirror jsHandler = scriptModuleInstance.useJs(jsHandlerFileFullName);
////        Object handlerObject = jsHandler.getMember(Handler_Method);
////        if (!(handlerObject instanceof ScriptObjectMirror)) {
////            return null;
////        }
////        ScriptObjectMirror handlerFunction = (ScriptObjectMirror) handlerObject;
////        if (!handlerFunction.isFunction()) {
////            return null;
////        }
////        if (handler instanceof HandlerMethod) {
////            log.warn("js请求处理函数功能被原生SpringMvc功能覆盖 | {}", jsHandlerFileFullName);
////            response.setHeader("http-request-js-handler-be-override", jsHandlerFileFullName);
////            return null;
////        }
////        if (!(handler instanceof ResourceHttpRequestHandler)) {
////            log.warn("出现意外的handler | {}", handler.getClass());
////            return null;
////        }
////        return jsHandler;
////    }
//
//    /**
//     * 使用js代码处理请求
//     */
//    private long doHandle(
//            final ScriptObjectMirror jsHandler,
//            final String jsHandlerFileFullName,
//            final HttpServletRequest request, final HttpServletResponse response) throws IOException {
////        final JsCodeFile jsCodeFile = getJsCodeFile(jsHandlerFileFullName);
////        if (jsCodeFile == null) {
////            throw new RuntimeException("JsCodeFile不存在");
////        }
////        ServletContextWrapper contextWrapper = new ServletContextWrapper(request, response, jacksonMapper);
////        Object result;
////        Long codeRunLogId = null;
////        try {
////            codeRunLogId = codeRunLogService.startLog(jsCodeFile);
////            result = jsHandler.callMember(Handler_Method, contextWrapper);
////        } catch (Throwable e) {
////            log.warn("执行jsHandler异常", e);
////            if (codeRunLogId != null) {
////                codeRunLogService.endLog(codeRunLogId, EnumConstant.Status_3);
////            }
////            throw ExceptionUtils.unchecked(e);
////        } finally {
////            if (codeRunLogId != null) {
////                codeRunLogService.endLog(codeRunLogId, EnumConstant.Status_2);
////            }
////        }
//        final long startTime = System.currentTimeMillis();
////        contextWrapper.getResponseWrapper().wrapper();
////        boolean needWriteResult = false;
////        if (result != null && !(result instanceof Undefined)) {
////            needWriteResult = true;
////        }
////        if (needWriteResult && !contextWrapper.getResponseWrapper().isWrite()) {
////            response.setContentType("application/json;charset=UTF-8");
////            response.getWriter().println(jacksonMapper.toJson(result));
////        }
//        return startTime;
//    }
//
//    @SuppressWarnings("NullableProblems")
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
//        // 1.判断请求是否支持 Script 处理
//        // 2.获取处理请求的 Script 文件全路径
//        // 3.获取 Script 文件对应的 Script 对象和执行函数名
//        // 4.执行 Script 对象的函数
//
//        String requestUri = request.getRequestURI();
//        boolean supportUri = false;
//        for (String suffix : Support_Suffix) {
//            if (StringUtils.isBlank(suffix)) {
//                supportUri = true;
//                continue;
//            }
//            if (requestUri.endsWith(suffix)) {
//                supportUri = true;
//                requestUri = requestUri.substring(0, requestUri.length() - suffix.length());
//                break;
//            }
//        }
////
////        // 获取JS文件全名称
////        final long startTime1 = System.currentTimeMillis();
////        final String scriptFileFullPath = getScriptFileFullPathUseCache(request);
////        if (StringUtils.isBlank(scriptFileFullPath)) {
////            final long tmp = System.currentTimeMillis() - startTime1;
////            if (tmp > 0) {
////                log.debug("跳过script处理 | 总耗时 {}ms", tmp);
////            }
////            return true;
////        }
////        // 获取js模块对象处理请求
////        final long startTime2 = System.currentTimeMillis();
////        final ScriptObjectMirror jsHandler = getScriptHandler(scriptFileFullPath, response, handler);
////        if (jsHandler == null) {
////            return true;
////        }
////        // 使用js代码处理请求
////        final long startTime3 = System.currentTimeMillis();
////        response.setHeader("use-http-request-js-handler", jsHandlerFileFullName);
////        final long startTime4 = doHandle(jsHandler, jsHandlerFileFullName, request, response);


//
////    @Override
////    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
////        log.debug("=================================================== postHandle");
////    }
////
////    @Override
////    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
////        log.debug("=================================================== afterCompletion");
////    }
//}
