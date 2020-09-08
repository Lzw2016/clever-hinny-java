package org.clever.hinny.mvc;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.boot.autoconfigure.web.format.WebConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.validation.BindException;
import org.springframework.validation.DataBinder;

import java.beans.PropertyEditor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/09/07 17:03 <br/>
 */
@Slf4j
public class DataBinderTest {

    @Test
    public void t00() throws BindException {
        Person model = new Person();
        DataBinder binder = new DataBinder(model, "model");
        MutablePropertyValues pvs = new MutablePropertyValues();
        pvs.add("name", "lzw");
        pvs.add("age", 18);
        pvs.add("address.country", "中国");
        pvs.add("address.city", "武汉");
//        MutablePropertyValues pvs_2 = new MutablePropertyValues();
//        pvs_2.add("country", "中国");
//        pvs_2.add("city", "武汉");
//        pvs.add("address", pvs_2);
        binder.bind(pvs);
        Map<?, ?> close = binder.close();

        log.info("close --> {}", close);
        log.info("model --> {}", model);
    }

    @Test
    public void t01() throws BindException {
        Map<String, Object> model = new HashMap<>();
        model.put("name", null);
        model.put("age", null);
        DataBinder binder = new DataBinder(model, "model");
        MutablePropertyValues pvs = new MutablePropertyValues();
        pvs.add("name", "lzw");
        pvs.add("age", 18);
        binder.bind(pvs);
        Map<?, ?> close = binder.close();

        log.info("close --> {}", close);
        log.info("model --> {}", model);
    }

    @Test
    public void t02() throws BindException {
        Map<String, Object> model = new HashMap<>();
        model.put("name", "");
        model.put("age", 0);
        MapDataBinder binder = new MapDataBinder(model);
        binder.setConversionService(new WebConversionService("yyyy-MM-dd"));
        MutablePropertyValues pvs = new MutablePropertyValues();
        pvs.add("name", "lzw");
        pvs.add("age", "18");
        MutablePropertyValues sub = new MutablePropertyValues();
        sub.add("aaa", "a");
        sub.add("bbb", "b");
        pvs.add("sub", sub);
        binder.bind(pvs);
        Map<?, ?> close = binder.close();

        log.info("close --> {}", close);
        log.info("model --> {}", model);
    }

    @Data
    public static class Person {
        private String name;
        private Integer age;
        private Address address;
    }

    @Data
    public static class Address {
        private String country;
        private String city;
    }

    @Test
    public void t03() {
        // org.springframework.beans.propertyeditors
        PropertyEditor editor = new CustomCollectionEditor(List.class);
        editor.setAsText("[1,2,3,4]");
        log.info("Value --> {}", editor.getValue());

//        // com.sun.beans.editors
//        editor = new FloatEditor();
//        editor.setAsText("123");
//        log.info("Value --> {}", editor.getValue());
    }

    @Test
    public void t04() {
        FormattingConversionService conversionService = new DefaultFormattingConversionService();
        log.info("--> {}", conversionService.convert("false", Boolean.class));

//        log.info("--> {}", conversionService.convert(new String[]{"111", "222"}, Integer[].class));
//        log.info("--> {}", conversionService.convert("222", Integer[].class));

                log.info("--> {}", conversionService.convert("222", List.class));
//        org.springframework.core.convert.converter.Converter
    }

// org.springframework.beans.PropertyEditorRegistrySupport
/*
com.sun.beans.editors
    BooleanEditor
    ByteEditor
    ColorEditor
    DoubleEditor
    EnumEditor
    FloatEditor
    FontEditor
    IntegerEditor
    LongEditor
    NumberEditor
    ShortEditor
    StringEditor
org.springframework.beans.propertyeditors
    ByteArrayPropertyEditor.java
    CharacterEditor.java
    CharArrayPropertyEditor.java
    CharsetEditor.java
    ClassArrayEditor.java
    ClassEditor.java
    CurrencyEditor.java
    CustomBooleanEditor.java
    CustomCollectionEditor.java
    CustomDateEditor.java
    CustomMapEditor.java
    CustomNumberEditor.java
    FileEditor.java
    InputSourceEditor.java
    InputStreamEditor.java
    LocaleEditor.java
    PathEditor.java
    PatternEditor.java
    PropertiesEditor.java
    ReaderEditor.java
    ResourceBundleEditor.java
    StringArrayPropertyEditor.java
    StringTrimmerEditor.java
    TimeZoneEditor.java
    URIEditor.java
    URLEditor.java
    UUIDEditor.java
    ZoneIdEditor.java
org.springframework.web.multipart.support
    ByteArrayMultipartFileEditor.java
    StringMultipartFileEditor.java
org.springframework.core.convert.support
    ConvertingPropertyEditorAdapter.java
org.springframework.core.io
    ResourceEditor.java
org.springframework.core.io.support
    ResourceArrayPropertyEditor.java
org.springframework.format.support
    FormatterPropertyEditorAdapter.java
org.springframework.mail.javamail
    InternetAddressEditor.java
org.springframework.beans
    PropertyValuesEditor.java
org.springframework.data.redis.core
    GeoOperationsEditor.java
    HashOperationsEditor.java
    ListOperationsEditor.java
    SetOperationsEditor.java
    ValueOperationsEditor.java
    ZSetOperationsEditor.java
*/
}
