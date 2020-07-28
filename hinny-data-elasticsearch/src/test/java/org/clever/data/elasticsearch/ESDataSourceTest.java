package org.clever.data.elasticsearch;

import lombok.extern.slf4j.Slf4j;
import org.clever.data.elasticsearch.support.DocRes;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.autoconfigure.elasticsearch.jest.JestProperties;

import java.io.IOException;
import java.time.Duration;
import java.util.*;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/07/12 12:11 <br/>
 */
@Slf4j
public class ESDataSourceTest {

    private ESDataSource esDataSource;

    @Before
    public void init() {
        JestProperties properties = new JestProperties();
        properties.setUris(Collections.singletonList("http://elasticsearch.msvc.top"));
        properties.setMultiThreaded(true);
        properties.setConnectionTimeout(Duration.ofSeconds(30));
        properties.setReadTimeout(Duration.ofSeconds(30));
        esDataSource = new ESDataSource(properties);
    }

    @After
    public void close() throws Exception {
        esDataSource.close();
    }

    @Test
    public void t01() throws IOException {
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
    }
}
