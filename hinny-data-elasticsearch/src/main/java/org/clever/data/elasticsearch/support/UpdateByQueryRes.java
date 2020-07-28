package org.clever.data.elasticsearch.support;

import io.searchbox.core.UpdateByQueryResult;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/07/12 11:57 <br/>
 */
public class UpdateByQueryRes extends AbstractESResult {
    public UpdateByQueryRes(UpdateByQueryResult jestResult) {
        super(jestResult);
    }

    // TODO 封装返回结果
}
