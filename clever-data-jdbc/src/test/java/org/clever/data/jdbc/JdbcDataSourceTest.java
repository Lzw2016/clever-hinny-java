package org.clever.data.jdbc;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zaxxer.hikari.HikariConfig;
import lombok.extern.slf4j.Slf4j;
import org.clever.common.model.request.QueryByPage;
import org.clever.common.model.request.QueryBySort;
import org.clever.data.jdbc.support.InsertResult;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.TransactionDefinition;

import java.util.*;

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
    public void transactionDelivery() {
        String sql = "select * from tb_order_main where user_agent_id = 22222222";
        jdbcDataSource.beginTX(status1 -> {
            Map<String, Object> queryMap = jdbcDataSource.queryMap(sql);
            log.info("### dataTmp1 -> {}", queryMap);
            String update2 = "update tb_order_main set store_id = 66666 where user_agent_id = 22222222 ";
            log.info("### update -> {} , res -> {}", jdbcDataSource.update(update2), jdbcDataSource.queryMap(sql));
            try {
                jdbcDataSource.beginTX(status2 -> {
                    String update3 = "update tb_order_main set store_id = 444444 where user_agent_id = 22222222 ";
                    log.info("### update -> {} , res -> {}", jdbcDataSource.update(update3), jdbcDataSource.queryMap(sql));
                    return null;
                }, TransactionDefinition.PROPAGATION_NEVER);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("异常,user_agent_id -> {}", queryMap.get("user_agent_id"));
                log.info("### 当前修改数据回滚 -> {}", jdbcDataSource.queryMap(sql));
                log.error("###     数据回滚为  -> {}", queryMap);
                throw e;
            }
            return null;
        }, TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        log.info("### res -> {}", jdbcDataSource.queryMap(sql));
    }

    @Test
    public void updateTable() {
        String sql = "select * from tb_order_main where user_agent_id = 22222222";
        log.info("### res -> {}", jdbcDataSource.queryMap(sql));
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("siteId", 2222);
        paramMap.put("storeId", 1111);
        Map<String, Object> where = new HashMap<>();
        where.put("user_agent_id", 22222222);
        where.put("order_id", 1149635824560267265L);
        int i = jdbcDataSource.beginTX(status -> jdbcDataSource.updateTable("tb_order_main", paramMap, where, true));
        log.info("### update -> {}  res -> {}", i, jdbcDataSource.queryMap(sql));
    }

    @Test
    public void batchUpdate() {
        String sql = "select * from tb_order_main where user_agent_id=:userAgentId and site_id=:siteId";
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("userAgentId", 22222222);
        paramMap.put("siteId", 2222);
        log.info("### res -> {}", jdbcDataSource.queryList(sql, paramMap));

        Map<String, Object> paramMap2 = new HashMap<>();
        paramMap2.put("userAgentId", 51233223);
        paramMap2.put("siteId", 1111111112);

        String update = "update tb_order_main set store_no=:storeNo where user_agent_id=:userAgentId and site_id=:siteId";
        paramMap.put("storeNo", "kjskjdls");
        paramMap2.put("storeNo", "kjskjdls222222222");
        Collection<Map<String, Object>> coll = new ArrayList<>();
        coll.add(paramMap);
        coll.add(paramMap2);
        int[] i = jdbcDataSource.beginTX(status -> jdbcDataSource.batchUpdate(update, coll), TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        log.info("### update -> {}  res -> {}", i, jdbcDataSource.queryList(sql, paramMap));
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

    @Test
    public void queryByPage() {
        String sql = "select * from tb_order_main where user_agent_id=:userAgentId";
        Map<String, Object> where = new HashMap<>();
        where.put("userAgentId", 22222222);
        QueryByPage page = new QueryByPage();
        IPage<Map<String, Object>> query = jdbcDataSource.queryByPage(sql, page, where, true);
        log.info("### res -> {}", query.getRecords().toString());
    }

    @Test
    public void insertTable() {
        Map<String, Object> insert = new HashMap<>();
        insert.put("name", "叶茂祥");
        insert.put("age", 21);
        jdbcDataSource.beginTX(status -> {
            InsertResult insertTable = jdbcDataSource.insertTable("test", insert);
            log.info("### res -> {} ,主键值key > {}", insertTable.getInsertCount(), insertTable.getKeyHolderValue());
            return null;
        });
        String sql = "select * from test where name = '叶茂祥'";
        // TODO query可以增加一个 根据  selectFilelds(需要查询的字段) tableName(查询的表名称)  fields(where参数值)  的方法提供便利吗?
        log.info("###  res -> {}", jdbcDataSource.queryMap(sql));
    }

    @Test
    public void insert() {
        String sql = "insert into test (name,age) values (:name , :age)";
        Map<String, Object> insert = new HashMap<>();
        insert.put("name", "危乐");
        insert.put("age", 15);
        jdbcDataSource.beginTX(status -> {
            InsertResult insertTable = jdbcDataSource.insert(sql, insert);
            log.info("### res -> {} ,主键值key > {}", insertTable.getInsertCount(), insertTable.getKeyHolderValue());
            return null;
        });
        String find = "select * from test where name = :name and age =:age";
        log.info("###  res -> {}", jdbcDataSource.queryMap(find, insert));
    }

    @Test
    public void injection() {
        String sql = "select * from tb_order_main where user_agent_id = :userAgentId";
        String sql2 = "select * from tb_order_main where store_no = :storeNo";
        Map<String, Object> select = new HashMap<>();
        select.put("userAgentId", "22222222 or 1=1");
        select.put("storeNo", "22222222 or 1=1");
        //无法注入😂😂😂😂😂    可以的
        log.info("###  res -> {}", jdbcDataSource.queryList(sql, select).toString());
        log.info("###  res -> {}", jdbcDataSource.queryList(sql2, select).toString());
    }

    @Test
    public void insertTables() {
        Map<String, Object> insert1 = new HashMap<>();
        insert1.put("name", "吴晓峰");
        insert1.put("age", 23);
        Map<String, Object> insert2 = new HashMap<>();
        insert2.put("name", "危乐");
        insert2.put("age", 15);
        Collection<Map<String, Object>> coll = new ArrayList<>();
        coll.add(insert1);
        coll.add(insert2);
        jdbcDataSource.beginTX(status -> {
            List<InsertResult> insertTable = jdbcDataSource.insertTables("test", coll);
            insertTable.forEach(e -> log.info("### res -> {} ,主键值key > {}", e.getInsertCount(), e.getKeyHolderValue()));
            return null;
        });
        String find = "select * from test where name = :name and age =:age";
        coll.forEach(e -> log.info("###  res -> {}", jdbcDataSource.queryMap(find, e)));
    }

    @Test
    public void queryCursor() {
        String sql = "select * from tb_order_main ";
        // fixme 游标查询时返回的 SqlLoggerUtils Total 返回数量始终为60??????
        jdbcDataSource.query(sql, rowData -> log.info("### 行号 -> {} ,数据 > {}", rowData.getRowCount(), rowData.getRowData().toString()));
    }

    @Test
    public void queryCursorList() {
        String sql = "select * from tb_order_main limit 200";
        // fixme 游标查询时返回的 SqlLoggerUtils Total 返回数量始终为60??????
        jdbcDataSource.query(sql, 5, batchData -> log.info("### 行号 -> {} ,数据 > {}", batchData.getRowCount(), batchData.getRowDataList().toString()));
    }
}
