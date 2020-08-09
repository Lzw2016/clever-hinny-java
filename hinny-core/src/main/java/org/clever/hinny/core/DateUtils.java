package org.clever.hinny.core;


import org.clever.common.utils.DateTimeUtils;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/07/28 22:32 <br/>
 */
public class DateUtils {
    public static final DateUtils Instance = new DateUtils();

    private DateUtils() {
    }

    /**
     * 得到当前时间的日期字符串，如：2016-4-27、2016-4-27 21:57:15<br/>
     *
     * @param pattern 日期格式字符串，如："yyyy-MM-dd" "HH:mm:ss" "E"
     */
    public String getCurrentDate(String pattern) {
        return DateTimeUtils.getCurrentDate(pattern);
    }

    /**
     * 得到当前时间的日期字符串，格式（yyyy-MM-dd）<br/>
     */
    public String getCurrentDate() {
        return DateTimeUtils.getCurrentDate();
    }
}
