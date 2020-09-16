package org.clever.hinny.mvc.support;

import org.clever.common.utils.DateTimeUtils;
import org.springframework.core.convert.converter.Converter;

import java.util.Date;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/09/16 15:10 <br/>
 */
public class StringToDateConverter implements Converter<String, Date> {
    public static final StringToDateConverter Instance = new StringToDateConverter();

    private StringToDateConverter() {
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Date convert(String source) {
        return DateTimeUtils.parseDate(source);
    }
}
