package org.clever.data.common;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/02/17 14:55 <br/>
 */
public abstract class AbstractDataSource implements AutoCloseable {
    /**
     * 当前数据源是否关闭
     */
    protected boolean closed = false;

    /**
     * 当前数据源是否关闭
     */
    public boolean isClosed() {
        return closed;
    }

    @Override
    public void close() throws Exception {
        closed = true;
    }
}
