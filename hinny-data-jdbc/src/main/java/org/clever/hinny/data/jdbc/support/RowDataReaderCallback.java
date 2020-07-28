package org.clever.hinny.data.jdbc.support;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/07/09 22:37 <br/>
 */
public class RowDataReaderCallback extends AbstractRowCountCallbackHandler {
    /**
     * 游标读取数据消费者
     */
    private final Consumer<RowData> consumer;

    public RowDataReaderCallback(Consumer<RowData> consumer) {
        this.consumer = consumer;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    protected void processRow(ResultSet rs, int rowNum) throws SQLException {
        Map<String, Object> rowData = getRowData(rs, rowNum);
        consumer.accept(new RowData(getColumnNames(), getColumnTypes(), getColumnCount(), rowData, this.getRowCount()));
    }
}
