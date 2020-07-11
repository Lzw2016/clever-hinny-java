package org.clever.data.jdbc;

import com.zaxxer.hikari.HikariConfig;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.model.request.QueryByPage;
import org.clever.common.model.request.QueryBySort;
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
        String sql = "select * from tb_order_main where site_id=:siteId and store_id=:storeId limit 10";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("siteId", 1111111112);
        paramMap.put("storeId", 1119829651059834882L);

        // 简单查询
        List<Map<String, Object>> res = jdbcDataSource.queryList(sql, paramMap);
        log.info("### res -> {}", res);

        // sql count 查询
        long count = jdbcDataSource.queryCount(sql, paramMap);
        log.info("### count -> {}", count);
    }

    @Test
    public void transaction() {
        String sql = "select * from tb_order_main where site_id=:siteId and store_id=:storeId limit 1";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("siteId", 1111111112);
        paramMap.put("storeId", 1119829651059834882L);

        // 简单事务
        Map<String, Object> data = jdbcDataSource.beginTX(status -> jdbcDataSource.queryMap(sql, paramMap));
        log.info("### data -> {}", data);

        // 嵌套事务
        jdbcDataSource.beginReadOnlyTX(status1 -> {
            Map<String, Object> dataTmp1 = jdbcDataSource.queryMap(sql, paramMap);
            log.info("### dataTmp1 -> {}", dataTmp1.size());
            // 开启新事物
            jdbcDataSource.beginTX(status2 -> {
                Map<String, Object> dataTmp2 = jdbcDataSource.queryMap(sql, paramMap);
                log.info("### dataTmp2 -> {}", dataTmp2.size());
                return null;
            }, TransactionDefinition.PROPAGATION_REQUIRES_NEW);
            return null;
        });
    }

    @Test
    public void queryOne() {
        String sql = "select * from tb_order_main where order_id = 1149635824560267265";
        // 简单查询
        log.info("### res -> {}", jdbcDataSource.queryMap(sql));
        // 开启事务 修改一条记录
        String updateSql = "update tb_order_main set user_agent_id = 22222222 where order_id = 1149635824560267265 ";
        Integer integer = jdbcDataSource.beginTX(status -> jdbcDataSource.update(updateSql));
        log.info("### update -> {} , res -> {}", integer, jdbcDataSource.queryMap(sql));
        // 无事务修改一条记录测试
        String update2 = "update tb_order_main set user_agent_id = 333333 where order_id = 1149635824560267265 ";
        log.info("### update -> {} , res -> {}", jdbcDataSource.update(update2), jdbcDataSource.queryMap(sql));
    }

    @Test
    public void updateTable() {
        String sql = "select * from tb_order_main where user_agent_id = 22222222";
        log.info("### res -> {}", jdbcDataSource.queryMap(sql));
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("siteId", 0);
        paramMap.put("storeId", 5644456);
        Map<String, Object> where = new HashMap<>();
        where.put("user_agent_id", 22222222);
        int i = jdbcDataSource.beginTX(status -> jdbcDataSource.updateTable("tb_order_main", paramMap, where, true));
        log.info("### update -> {}  res -> {}", i, jdbcDataSource.queryMap(sql));
    }

    @Test
    public void queryPage() {
        String sql = "select * from tb_order_main";
        QueryByPage pagination = new QueryByPage();
        pagination.setPageSize(5);
        pagination.addOrderFieldMapping("storeId", "store_id");
        pagination.addOrderField("storeId", QueryBySort.DESC);
        log.info("### res -> {}", jdbcDataSource.queryByPage(sql, pagination));
    }

    @Test
    public void queryBySort() {
        String sql = "select * from tb_order_main where user_agent_id=22222222";
        QueryBySort queryByPage = new QueryBySort();
        queryByPage.addOrderFieldMapping("storeId", "store_id");
        queryByPage.addOrderField("storeId", QueryBySort.DESC);
        log.info("### res -> {}", jdbcDataSource.queryBySort(sql, queryByPage));
    }
}
