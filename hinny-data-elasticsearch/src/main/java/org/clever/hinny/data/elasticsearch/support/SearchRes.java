package org.clever.hinny.data.elasticsearch.support;

import io.searchbox.core.SearchResult;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/07/12 12:02 <br/>
 */
public class SearchRes extends AbstractESResult {
    public SearchRes(SearchResult jestResult) {
        super(jestResult);
    }

    // TODO 封装返回结果
}
