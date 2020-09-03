package org.clever.hinny.data.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOCase;
import org.clever.hinny.data.jdbc.dynamic.MyBatisMapperSql;
import org.clever.hinny.data.jdbc.dynamic.watch.FileSystemWatcher;
import org.junit.Test;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/09/02 16:37 <br/>
 */
@Slf4j
public class MyBatisMapperSqlTest {

    @Test
    public void t01() throws InterruptedException {
        String absolutePath = "D:\\SourceCode\\jzt\\门店通\\pharmacy-points-shop-ba\\server\\src";
        final MyBatisMapperSql sqlUtils = new MyBatisMapperSql(absolutePath);
        log.info("sqlUtils -> {}", sqlUtils);
        FileSystemWatcher watcher = new FileSystemWatcher(
                absolutePath,
                file -> {
                    try {
                        sqlUtils.reloadFile(file.getAbsolutePath());
                    } catch (Exception e) {
                        log.error("", e);
                    }
                },
                new String[]{"*.xml"},
                new String[]{},
                IOCase.SYSTEM,
                1000
        );
        watcher.start();
        Thread.sleep(1000 * 60 * 5);
    }
}
