# clever-hinny-java

> clever-hinny项目Java生态封装层

## hinny-data-*

> 封装常用数据库中间件API，为在函数式编程环境中提供各种数据库操作的便捷API。
> 当前项目每个子模块对应一个数据库的封装(`hinny-data-common`模块除外)

### 快速开始

## hinny-data-jdbc

1. 主要功能：操作关系型数据库
1. 主要API: JdbcDataSource

#### 使用demo

```java
public void demo() {
    HikariConfig hikariConfig = new HikariConfig();
    hikariConfig.setDriverClassName("com.mysql.cj.jdbc.Driver");
    // ...省略配置
    jdbcDataSource = new JdbcDataSource(hikariConfig);

    String sql = "select * from tb_order_main where site_id=:siteId and store_id=:storeId limit 10";
    Map<String, Object> paramMap = new HashMap<>();
    paramMap.put("siteId", 1111111112);
    paramMap.put("storeId", 1119829651059834882L);

    // 简单查询
    List<Map<String, Object>> res = jdbcDataSource.queryList(sql, paramMap);
    log.info("### res -> {}", res);
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
    // 关闭数据源
    jdbcDataSource.close();
}
```

## hinny-data-redis

1. 主要功能：操作Redis数据库
1. 主要API: RedisDataSource

#### 使用demo

```java
public void demo() {
    RedisProperties properties = new RedisProperties();
    properties.setPort(6379);
    // ...省略配置
    pool.setMaxWait(Duration.ofSeconds(1));
    properties.getLettuce().setPool(pool);
    redisDataSource = new RedisDataSource(properties);
    // 使用demo
    String key = "lzw-123456";
    for (int i = 0; i < 10; i++) {
        redisDataSource.hPut(key, "k_" + i, "v_" + i);
    }
    boolean has = redisDataSource.kHasKey(key);
    log.info("### has -> {}", has);
    boolean del = redisDataSource.kDelete(key);
    log.info("### del -> {}", del);
    has = redisDataSource.kHasKey(key);
    log.info("### has -> {}", has);
    // 关闭数据源
    redisDataSource.close();
}
```

## hinny-data-elasticsearch

1. 主要功能：操作ElasticSearch数据库
1. 主要API: ESDataSource

```java
public void demo() {
    JestProperties properties = new JestProperties();
    properties.setMultiThreaded(true);
    properties.setConnectionTimeout(Duration.ofSeconds(30));
    // ...省略配置
    esDataSource = new ESDataSource(properties);
    // 使用demo
    String index = "test-ymx";
    String type = "role";
    String id = "1";
    DocRes docRes = esDataSource.getData(index, id);
    log.info("### docRes -> {}", docRes.getJestResult().getJsonObject());
    Map<String, Object> source = new HashMap<>();
    source.put("name", UUID.randomUUID().toString());
    source.put("description", "测试");
    source.put("create_at", new Date());
    source.put("int", 123);
    source.put("boolean", false);
    source.put("double", 12.36D);
    docRes = esDataSource.addDoc(index, type, source);
    log.info("### docRes -> {}", docRes.getJestResult().getJsonObject());
    // 关闭数据源
    esDataSource.close();
}
```

## hinny-data-rabbitmq

1. 主要功能：操作RabbitMQ消息队列
1. 主要API: RabbitMqDataSource

## hinny-data-kafka

1. 主要功能：操作Kafka消息队列
1. 主要API: KafkaDataSource
