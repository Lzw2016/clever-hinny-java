package org.clever.hinny.meta.data.convert;

import org.clever.hinny.meta.data.model.TableSchema;
import schemacrawler.schema.Table;
import schemacrawler.schema.View;

/**
 * 作者：lizw <br/>
 * 创建时间：2020-10-01 19:14 <br/>
 */
public class ConvertToTableSchema {
    public static TableSchema convert(Table table) {
        TableSchema tableSchema = new TableSchema();
        tableSchema.setView(table instanceof View);
        tableSchema.setSchemaName(table.getSchema().getFullName());
        tableSchema.setTableName(table.getName());
        tableSchema.setDescription(table.getRemarks());
        return tableSchema;
    }
}
