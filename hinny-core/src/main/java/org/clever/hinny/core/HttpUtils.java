package org.clever.hinny.core;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/07/28 22:32 <br/>
 */
public class HttpUtils {
    public static final HttpUtils Instance = new HttpUtils();

    private final org.clever.common.utils.HttpUtils delegate;

    private HttpUtils() {
        delegate = org.clever.common.utils.HttpUtils.getInner();
    }

    public void test() {
    }

}
