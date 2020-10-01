package org.clever.hinny.meta.data.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 作者：lizw <br/>
 * 创建时间：2020-10-01 16:04 <br/>
 */
@Data
public class TableSchema implements Serializable {
    /**
     * 是否是视图
     */
    private boolean isView = false;
    /**
     * 数据库名称
     */
    private String schemaName;
    /**
     * 表名称
     */
    private String tableName;
    /**
     * 表注释说明
     */
    private String description;
    /**
     * 数据库列
     */
    private final List<TableColumn> columnList = new ArrayList<>();
}
