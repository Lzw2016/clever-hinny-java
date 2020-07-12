package org.clever.data.elasticsearch.support;

import io.searchbox.client.JestResult;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/07/12 12:00 <br/>
 */
public class DeleteByQueryRes extends AbstractESResult {
    public DeleteByQueryRes(JestResult jestResult) {
        super(jestResult);
    }

    // TODO 封装返回结果
}
