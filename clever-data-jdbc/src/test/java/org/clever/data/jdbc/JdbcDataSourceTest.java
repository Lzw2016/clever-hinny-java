package org.clever.data.jdbc;

import com.zaxxer.hikari.HikariConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.TransactionDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/07/10 13:21 <br/>
 */
@Slf4j
public class JdbcDataSourceTest {

    private JdbcDataSource jdbcDataSource;

    @Before
    public void init() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
        hikariConfig.setJdbcUrl("jdbc:mysql://mysql.msvc.top:3306/clever-template");
        hikariConfig.setUsername("clever-template");
        hikariConfig.setPassword("lizhiwei1993");
        hikariConfig.setAutoCommit(false);
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setMinimumIdle(5);
        hikariConfig.setMaxLifetime(1800_000);
        hikariConfig.setConnectionTestQuery("SELECT 1");
        hikariConfig.getDataSourceProperties().put("serverTimezone", "GMT+8");
        hikariConfig.getDataSourceProperties().put("useUnicode", "true");
        hikariConfig.getDataSourceProperties().put("characterEncoding", "utf-8");
        hikariConfig.getDataSourceProperties().put("zeroDateTimeBehavior", "convert_to_null");
        hikariConfig.getDataSourceProperties().put("useSSL", "false");
        jdbcDataSource = new JdbcDataSource(hikariConfig);
    }

    @After
    public void close() throws Exception {
        jdbcDataSource.close();
    }

    @Test
    public void query() {
        String sql = "select * from tb_order_main where site_id=:siteId limit 10";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("siteId", 1111111112);

        // 简单查询
        List<Map<String, Object>> res = jdbcDataSource.queryList(sql, paramMap);
        log.info("### res -> {}", res);

        // sql count 查询
        long count = jdbcDataSource.queryCount(sql, paramMap);
        log.info("### count -> {}", count);
    }

    @Test
    public void transaction() {
        String sql = "select * from tb_order_main where site_id=1111111112 limit 1";

        // 简单事务
        Map<String, Object> data = jdbcDataSource.beginTX(status -> jdbcDataSource.queryMap(sql));
        log.info("### data -> {}", data);

        // 嵌套事务
        jdbcDataSource.readOnlyTX(status1 -> {
            Map<String, Object> dataTmp1 = jdbcDataSource.queryMap(sql);
            log.info("### dataTmp1 -> {}", dataTmp1.size());
            // 开启新事物
            jdbcDataSource.beginTX(status2 -> {
                Map<String, Object> dataTmp2 = jdbcDataSource.queryMap(sql);
                log.info("### dataTmp2 -> {}", dataTmp2.size());
                return null;
            }, TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            return null;
        });
    }
}
