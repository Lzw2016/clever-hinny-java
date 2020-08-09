package org.clever.hinny.core;

import java.util.Date;
import java.util.Objects;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/07/28 22:32 <br/>
 */
public class CommonUtils {
    public static final CommonUtils Instance = new CommonUtils();

    private CommonUtils() {
    }

    /**
     * 休眠一段时间
     *
     * @param millis 毫秒
     */
    public void sleep(Number millis) throws InterruptedException {
        Thread.sleep(millis.longValue());
    }

    /**
     * 获取对象的 hashcode
     */
    public Integer hashCode(Object object) {
        if (object == null) {
            return null;
        }
        return object.hashCode();
    }

    /**
     * 两个对象 equals
     */
    public boolean equals(Object a, Object b) {
        return Objects.equals(a, b);
    }

    /**
     * 判断两个对象是不是同一个对象(内存地址相同)
     */
    public boolean same(Object a, Object b) {
        return a == b;
    }

    /**
     * 获取当前时间搓(毫秒)
     */
    public long currentTimeMillis() {
        return System.currentTimeMillis();
    }

    /**
     * 获取当前时间 Date
     */
    public Date now() {
        return new Date();
    }
}
