package org.clever.hinny.data.elasticsearch.support;

import io.searchbox.client.JestResult;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/07/12 11:26 <br/>
 */
public class IndexRes extends AbstractESResult {

    public IndexRes(JestResult jestResult) {
        super(jestResult);
    }

    // TODO 封装返回结果
}
