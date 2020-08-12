package org.clever.hinny.core;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/08/12 20:32 <br/>
 */
@Slf4j
public class XmlUtilsTest {

    @Test
    public void t01() {
        Map<String, Object> map = new HashMap<>();
        map.put("string", "abc123");
        map.put("int", 123456);
        map.put("boolean", false);
        log.info("map  -> {}", XmlUtils.Instance.toXml(map));
        log.info("map  -> {}", JsonUtils.Instance.toJson(map));

        log.info("xmlToJson  -> {}", XmlUtils.Instance.xmlToJson(XmlUtils.Instance.toXml(map)));
        log.info("jsonToXml  -> {}", JsonUtils.Instance.jsonToXml(JsonUtils.Instance.toJson(map)));

        List<Object> list = new ArrayList<>();
        list.add("abc123");
        list.add(123456);
        list.add(false);
        log.info("list -> {}", XmlUtils.Instance.toXml(list));
        log.info("list -> {}", JsonUtils.Instance.toJson(list));

        log.info("xmlToJson  -> {}", XmlUtils.Instance.xmlToJson(XmlUtils.Instance.toXml(list)));
        log.info("jsonToXml  -> {}", JsonUtils.Instance.jsonToXml(JsonUtils.Instance.toJson(list)));
    }
}
