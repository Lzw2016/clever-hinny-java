package org.clever.hinny.data.jdbc.support;

import com.baomidou.mybatisplus.annotation.DbType;
import lombok.Data;

import java.io.Serializable;

@Data
public class JdbcInfo implements Serializable {
    private String driverClassName;

    private String jdbcUrl;

    private boolean isAutoCommit;

    private boolean isReadOnly;

    private DbType dbType;

    private boolean isClosed;
}
