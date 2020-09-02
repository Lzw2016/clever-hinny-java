package org.clever.hinny.data.jdbc.dynamic;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.clever.dynamic.sql.BoundSql;
import org.clever.dynamic.sql.DynamicSqlParser;
import org.clever.dynamic.sql.builder.SqlSource;
import org.clever.dynamic.sql.parsing.XNode;
import org.clever.dynamic.sql.parsing.XPathParser;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/09/02 15:56 <br/>
 */
@Slf4j
public class MyBatisMapperSql {
    /**
     * 文件根路径
     */
    protected final File path;
    /**
     * 所有的Mapper文件 {@code MultiValueMap<Mapper文件绝对路径, SqlId>}
     */
    protected final MultiValueMap<String, String> mapperFiles = new LinkedMultiValueMap<>(32);
    /**
     * SqlSource对象 {@code Map<SqlId, SqlSource对象>}
     */
    protected final ConcurrentHashMap<String, SqlSource> sqlSourceMap = new ConcurrentHashMap<>(64);

    public MyBatisMapperSql(String absolutePath) {
        this.path = new File(absolutePath);
        Assert.isTrue(path.exists() && path.isDirectory(), "路径：" + path.getAbsolutePath() + "不存在或者不是一个文件夹");
        load();
    }

    /**
     * 获取 SqlSource
     */
    public SqlSource getSqlSource(String sqlId) {
        return sqlSourceMap.get(sqlId);
    }

    /**
     * 获取 SqlSource
     */
    public BoundSql getSqlSource(String sqlId, Object parameter) {
        SqlSource sqlSource = sqlSourceMap.get(sqlId);
        if (sqlSource == null) {
            return null;
        }
        return sqlSource.getBoundSql(parameter);
    }

    public void reloadAll() {
        mapperFiles.clear();
        sqlSourceMap.clear();
        Collection<File> files = FileUtils.listFiles(path, new String[]{"xml"}, true);
        for (File file : files) {
            final String absolutePath = file.getAbsolutePath();
            log.info("# 解析文件: {}", absolutePath);
            try {
                loadSqlSource(file);
            } catch (Exception e) {
                log.error("解析Mapper.xml文件失败 | path={}", absolutePath);
            }
        }
    }

    @SneakyThrows
    public void reloadFile(String absolutePath) {
        if (absolutePath == null || !absolutePath.endsWith(".xml")) {
            return;
        }
        final boolean preExists = mapperFiles.containsKey(absolutePath);
        final File file = new File(absolutePath);
        final boolean nowExists = file.exists() && file.isFile();
        // 删除之前的 SqlId
        if (preExists) {
            List<String> sqlIds = mapperFiles.get(absolutePath);
            if (sqlIds != null) {
                sqlIds.forEach(sqlSourceMap::remove);
            }
            mapperFiles.remove(absolutePath);
        }
        // 解析新的文件
        if (nowExists) {
            loadSqlSource(file);
        }
    }

    protected void load() {
        log.info("# ==================================================================================================================================");
        log.info("# 初始化读取Mapper.xml文件");
        log.info("# ==================================================================================================================================");
        long startTime = System.currentTimeMillis();
        reloadAll();
        long endTime = System.currentTimeMillis();
        log.info("# ==================================================================================================================================");
        log.info("# 读取Mapper.xml文件完成 | 耗时: {}ms", endTime - startTime);
        log.info("# ==================================================================================================================================");
    }

    protected void loadSqlSource(File file) throws Exception {
        final String absolutePath = file.getAbsolutePath();
        final Properties variables = new Properties();
        final String xml = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
        final XPathParser parser = new XPathParser(xml, false, variables);
        final XNode mapper = parser.evalNode("/mapper");
        if (mapper == null) {
            return;
        }
        final String namespace = mapper.getStringAttribute("namespace", "");
        if (StringUtils.isBlank(namespace)) {
            log.warn("# Mapper.xml文件未设置namespace属性 | path={}", absolutePath);
        }
        final List<XNode> nodes = mapper.evalNodes("sql|select|insert|update|delete");
        if (nodes == null) {
            return;
        }
        for (XNode node : nodes) {
            final String name = node.getName();
            final String id = node.getStringAttribute("id", "");
            if (StringUtils.isBlank(namespace)) {
                log.warn("# Mapper.xml文件<{}>未设置id属性 | path={}", name, absolutePath);
            }
            if (StringUtils.isBlank(namespace) && StringUtils.isBlank(id)) {
                log.warn("# Mapper.xml文件<{}> SqlId为空忽略该SQL | path={}", name, absolutePath);
                continue;
            }
            final StringBuilder sqlIdSB = new StringBuilder(namespace.length() + id.length());
            if (StringUtils.isNotBlank(namespace)) {
                sqlIdSB.append(StringUtils.trim(namespace)).append(".");
            }
            if (StringUtils.isNotBlank(id)) {
                sqlIdSB.append(StringUtils.trim(id));
            }
            final String sqlId = sqlIdSB.toString();
            SqlSource sqlSource = DynamicSqlParser.parserSql(node);
            if (sqlSourceMap.containsKey(sqlId)) {
                log.warn("# SQL出现冲突(覆盖) | SqlId={} | path={}", sqlId, absolutePath);
            }
            mapperFiles.add(absolutePath, sqlId);
            sqlSourceMap.put(sqlId, sqlSource);
            log.info("# SQL读取成功,SqlId: {}", sqlId);
        }
    }
}
