package org.clever.data.elasticsearch.support;

import io.searchbox.client.JestResult;
import lombok.Getter;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/07/12 11:25 <br/>
 */
public abstract class AbstractESResult {
    @Getter
    protected final JestResult jestResult;

    public AbstractESResult(JestResult jestResult) {
        this.jestResult = jestResult;
    }

    // TODO 封装返回结果
}
