package org.clever.hinny.core;

import org.clever.common.utils.mapper.XStreamMapper;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/08/12 09:07 <br/>
 */
public class XmlUtils {

    public static final XmlUtils Instance = new XmlUtils();

    private final XStreamMapper mapper;

    private XmlUtils() {
        mapper = XStreamMapper.getDom4jXStream();
    }

    /**
     * 对象序列化成XML字符串
     *
     * @param object 需要序列化xml的对象
     * @return 返回xml
     */
    public String toXml(Object object) {
        return mapper.toXml(object);
    }

    /**
     * XML字符串反序列化成对象
     *
     * @param xmlString XML字符串
     * @param <T>       返回的对象类型
     * @return 返回的对象
     */
    public <T> T fromXml(String xmlString) {
        return mapper.fromXml(xmlString);
    }

//    public boolean update(String xmlString, Object object) {

}
