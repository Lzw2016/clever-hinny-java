package org.clever.data.elasticsearch.support;

import io.searchbox.client.JestResult;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/07/12 12:05 <br/>
 */
public class MultiGetRes extends AbstractESResult {
    public MultiGetRes(JestResult jestResult) {
        super(jestResult);
    }

    // TODO 封装返回结果
}
