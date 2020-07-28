package org.clever.hinny.data.elasticsearch.support;

import io.searchbox.core.DocumentResult;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/07/12 11:48 <br/>
 */
public class DocRes extends AbstractESResult {
    public DocRes(DocumentResult jestResult) {
        super(jestResult);
    }

    // TODO 封装返回结果
}
