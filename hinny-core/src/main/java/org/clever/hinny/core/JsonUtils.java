package org.clever.hinny.core;

import org.clever.common.utils.mapper.JacksonMapper;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/08/12 09:07 <br/>
 */
public class JsonUtils {
    public static final JsonUtils Instance = new JsonUtils();

    private final JacksonMapper jacksonMapper;

    private JsonUtils() {
        jacksonMapper = JacksonMapper.getInstance();
    }

    /**
     * Object可以是POJO，也可以是Collection或数组。 如果对象为Null, 返回"null". 如果集合为空集合, 返回"[]".<br/>
     *
     * @param object 需要序列化的对象
     * @return 序列化后的Json字符串
     */
    public String toJson(Object object) {
        return jacksonMapper.toJson(object);
    }

    /**
     * 输出JSON格式数据.
     */
    public String toJsonP(String functionName, Object object) {
        return jacksonMapper.toJsonP(functionName, object);
    }

    /**
     * 把Json字符串转换成Map对象
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> toMap(String json) {
        return jacksonMapper.fromJson(json, LinkedHashMap.class);
    }

//    public boolean update(String jsonString, Object object) {
}
