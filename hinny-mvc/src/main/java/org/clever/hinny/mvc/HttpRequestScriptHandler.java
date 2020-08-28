package org.clever.hinny.mvc;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.utils.mapper.JacksonMapper;
import org.clever.hinny.api.ScriptEngineInstance;
import org.clever.hinny.api.ScriptObject;
import org.clever.hinny.api.pool.EngineInstancePool;
import org.clever.hinny.mvc.http.HttpContext;
import org.clever.hinny.mvc.support.TupleTow;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/08/24 21:34 <br/>
 *
 * @param <E> script引擎类型
 * @param <T> script引擎对象类型
 */
@Slf4j
public abstract class HttpRequestScriptHandler<E, T> implements HandlerInterceptor {
    /**
     * 是否强制使用Script Handler处理请求
     */
    private static final String Force_Use_Script = "force-use-script";
    /**
     * 使用Script Handler处理请求时对应的Script File信息(响应头信息)
     */
    private static final String Use_Script_Handler_Head = "use-script-handler-file";
    /**
     * 分隔Script的File Path和MethodName的分隔符
     */
    private static final String Separate = "@";
    /**
     * 支持的请求前缀
     */
    private final String supportPrefix;
    /**
     * 支持的请求后缀
     */
    private final Set<String> supportSuffix;
    /**
     * 引擎实例对象池
     */
    private final EngineInstancePool<E, T> engineInstancePool;

    /**
     * @param supportPrefix      支持的请求前缀
     * @param supportSuffix      支持的请求后缀
     * @param engineInstancePool 引擎实例对象池
     */
    public HttpRequestScriptHandler(String supportPrefix, Set<String> supportSuffix, EngineInstancePool<E, T> engineInstancePool) {
        Assert.notNull(engineInstancePool, "参数engineInstancePool不能为空");
        this.supportPrefix = StringUtils.isNotBlank(supportPrefix) ? StringUtils.trim(supportPrefix) : "/!";
        supportSuffix = supportSuffix != null ? supportSuffix : new HashSet<>(Arrays.asList("", ".json", ".action"));
        supportSuffix = supportSuffix.stream().filter(Objects::nonNull).map(StringUtils::trim).collect(Collectors.toSet());
        this.supportSuffix = Collections.unmodifiableSet(supportSuffix);
        this.engineInstancePool = engineInstancePool;
    }

    /**
     * @param engineInstancePool 引擎实例对象池
     */
    public HttpRequestScriptHandler(EngineInstancePool<E, T> engineInstancePool) {
        this(null, null, engineInstancePool);
    }

    /**
     * 判断请求是否支持 Script 处理
     */
    protected boolean supportScript(HttpServletRequest request, HttpServletResponse response, Object handler) {
        final String requestUri = request.getRequestURI();
        // final String method = request.getMethod();
        boolean support = false;
        // 支持的请求前缀 - 符合
        if (!requestUri.startsWith(supportPrefix)) {
            return false;
        }
        // 支持的请求后缀 - 符合
        for (String suffix : supportSuffix) {
            if (requestUri.endsWith(suffix)) {
                support = true;
                break;
            }
        }
        // SpringMvc功能冲突处理
        if (support) {
            if (handler instanceof HandlerMethod) {
                if (StringUtils.isBlank(request.getParameter(Force_Use_Script)) || StringUtils.isBlank(request.getHeader(Force_Use_Script))) {
                    log.warn("强制使用Script Handler功能，忽略原生SpringMvc功能 | {}", handler.getClass());
                } else {
                    log.warn("Script Handler被原生SpringMvc功能覆盖 | {}", handler.getClass());
                    support = false;
                }
            } else if (handler instanceof ResourceHttpRequestHandler) {
                if (StringUtils.isBlank(request.getParameter(Force_Use_Script)) || StringUtils.isBlank(request.getHeader(Force_Use_Script))) {
                    log.warn("强制使用Script Handler功能，忽略静态资源 | {}", handler.getClass());
                } else {
                    log.warn("Script Handler被静态资源覆盖 | {}", handler.getClass());
                    support = false;
                }
            } else {
                log.warn("未知的Handler类型，覆盖Script Handler | {}", handler.getClass());
                support = false;
            }
        }
        // 请求Url格式 - 符合
        if (support) {
            int position = requestUri.lastIndexOf("/");
            if (position <= -1) {
                support = false;
            } else {
                String lastPath = requestUri.substring(position);
                String[] arr = lastPath.split(Separate);
                if (arr.length != 2) {
                    support = false;
                }
            }
        }
        return support;
    }

    /**
     * 获取处理请求的 Script 文件全路径<br/>
     * {@code TupleTow<ScriptFileFullPath, MethodName>}
     */
    protected TupleTow<String, String> getScriptInfo(HttpServletRequest request) {
        // 根据请求url解析 scriptInfo(filePath, method)
        String requestUri = request.getRequestURI();
        for (String suffix : supportSuffix) {
            if (StringUtils.isBlank(suffix)) {
                continue;
            }
            if (requestUri.endsWith(suffix)) {
                requestUri = requestUri.substring(0, requestUri.length() - suffix.length());
                break;
            }
        }
        final String requestPath = requestUri;
        String method = null;
        int position = requestPath.lastIndexOf("/");
        if (position >= 0) {
            String lastPath = requestPath.substring(position);
            String[] arr = lastPath.split(Separate);
            if (arr.length == 2) {
                method = arr[1];
            }
        }
        if (StringUtils.isBlank(method)) {
            return null;
        }
        final String filePath = requestPath.substring(0, requestPath.length() - (Separate.length() + method.length()));
        if (StringUtils.isBlank(filePath)) {
            return null;
        }
        TupleTow<String, String> scriptInfo = TupleTow.creat(filePath, method);
        // 判断文件是否存在
        if (fileExists(filePath)) {
            return scriptInfo;
        }
        return null;
    }

    /**
     * 判断Script File是否存在
     *
     * @param fullPath 文件全路径
     */
    protected abstract boolean fileExists(String fullPath);

    /**
     * 获取 Script 文件对应的 Script 对象和执行函数名
     */
    protected TupleTow<ScriptObject<T>, String> getScriptObject(
            HttpServletRequest request,
            ScriptEngineInstance<E, T> engineInstance,
            TupleTow<String, String> scriptInfo) throws Exception {
        ScriptObject<T> scriptObject = engineInstance.require(scriptInfo.getValue1());
        return TupleTow.creat(scriptObject, scriptInfo.getValue2());
    }

    /**
     * 执行 Script 对象的函数
     */
    protected Object doHandle(TupleTow<ScriptObject<T>, String> handlerScript, HttpContext httpContext) {
        ScriptObject<T> scriptObject = handlerScript.getValue1();
        String method = handlerScript.getValue2();
        return scriptObject.callMember(method, httpContext);
    }

    /**
     * 借一个引擎实例
     */
    protected ScriptEngineInstance<E, T> borrowEngineInstance() throws Exception {
        return engineInstancePool.borrowObject();
    }

    /**
     * 归还引擎实例(有借有还)
     */
    protected void returnEngineInstance(ScriptEngineInstance<E, T> engineInstance) {
        try {
            engineInstancePool.returnObject(engineInstance);
        } catch (Exception e) {
            log.error("归还ScriptEngineInstance失败", e);
        }
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.判断请求是否支持 Script 处理
        if (!supportScript(request, response, handler)) {
            return true;
        }
        long startTime1;         // 开始查找脚本文件时间
        long startTime2 = -1;    // 开始借一个引擎实例时间
        long startTime3 = -1;    // 开始加载脚本对象时间
        long startTime4 = -1;    // 开始执行脚本时间
        long startTime5 = -1;    // 开始序列化返回值时间
        // 2.获取处理请求的 Script 文件全路径和执行函数名
        startTime1 = System.currentTimeMillis();
        final TupleTow<String, String> scriptInfo = getScriptInfo(request);
        if (scriptInfo == null || StringUtils.isBlank(scriptInfo.getValue1()) || StringUtils.isBlank(scriptInfo.getValue2())) {
            final long tmp = System.currentTimeMillis() - startTime1;
            if (tmp > 0) {
                log.debug("Script Handler不存在 | 总耗时 {}ms", tmp);
            }
            return true;
        }
        ScriptEngineInstance<E, T> engineInstance = null;
        try {
            // 3.借一个引擎实例
            startTime2 = System.currentTimeMillis();
            engineInstance = borrowEngineInstance();
            if (engineInstance == null) {
                response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
                throw new RuntimeException("无法借到ScriptEngineInstance");
            }
            // 4.获取 Script 文件对应的 Script 对象和执行函数名
            startTime3 = System.currentTimeMillis();
            final TupleTow<ScriptObject<T>, String> scriptHandler = getScriptObject(request, engineInstance, scriptInfo);
            if (scriptHandler == null) {
                log.warn("获取Script Handler对象失败");
                return true;
            }
            // 5.执行 Script 对象的函数
            response.setHeader(Use_Script_Handler_Head, String.format("%s#%s", scriptInfo.getValue1(), scriptInfo.getValue2()));
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
            final long howLongSum = endTime - startTime1;                           // 总耗时
            final long howLong1 = startTime2 - startTime1;                          // 查找脚本耗时
            final long howLong2 = startTime3 <= -1 ? -1 : startTime3 - startTime2;  // 借一个引擎耗时
            final long howLong3 = startTime4 <= -1 ? -1 : startTime4 - startTime3;  // 加载脚本耗时
            final long howLong4 = startTime5 <= -1 ? -1 : startTime5 - startTime4;  // 执行脚本耗时
            final long howLong5 = endTime - startTime5;                             // 序列化耗时
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
