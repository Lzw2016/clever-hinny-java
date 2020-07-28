package org.clever.hinny.data.elasticsearch;

import com.google.gson.Gson;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.JestResult;
import io.searchbox.core.*;
import io.searchbox.core.search.sort.Sort;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.hinny.data.common.AbstractDataSource;
import org.clever.hinny.data.elasticsearch.support.*;
import org.springframework.boot.autoconfigure.elasticsearch.jest.HttpClientConfigBuilderCustomizer;
import org.springframework.boot.autoconfigure.elasticsearch.jest.JestProperties;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 作者：lizw <br/>
 * 创建时间：2019/10/22 16:54 <br/>
 */
@SuppressWarnings("DuplicatedCode")
@Slf4j
public class ESDataSource extends AbstractDataSource {

    private final JestClientBuilder jestClientBuilder;
    private final JestClient jestClient;

    /**
     * @param properties         ElasticSearch配置
     * @param gson               JSON序列化/反序列化Gson对象
     * @param builderCustomizers 自定义配置
     */
    public ESDataSource(JestProperties properties, Gson gson, List<HttpClientConfigBuilderCustomizer> builderCustomizers) {
        this.jestClientBuilder = new JestClientBuilder(properties, gson, builderCustomizers);
        this.jestClient = this.jestClientBuilder.getJestClient();
        initCheck();
    }

    /**
     * @param properties ElasticSearch配置
     * @param gson       JSON序列化/反序列化Gson对象
     */
    public ESDataSource(JestProperties properties, Gson gson) {
        this(properties, gson, null);
    }

    /**
     * @param properties ElasticSearch配置
     */
    public ESDataSource(JestProperties properties) {
        this(properties, null, null);
    }

    public ESDataSource(JestClientFactory jestClientFactory) {
        this.jestClientBuilder = new JestClientBuilder(jestClientFactory);
        this.jestClient = this.jestClientBuilder.getJestClient();
        initCheck();
    }

    public ESDataSource(JestClient jestClient) {
        this.jestClientBuilder = new JestClientBuilder(jestClient);
        this.jestClient = this.jestClientBuilder.getJestClient();
        initCheck();
    }

    @Override
    public void initCheck() {
        Ping ping = new Ping.Builder().build();
        try {
            JestResult jestResult = jestClient.execute(ping);
            log.debug("ping -> {}", jestResult.getResponseCode());
        } catch (IOException e) {
            throw new RuntimeException("ESDataSource创建失败", e);
        }
    }

    @Override
    public void close() throws Exception {
        if (closed) {
            return;
        }
        super.close();
        jestClient.close();
    }

    // --------------------------------------------------------------------------------------------
    // 索引 操作
    // --------------------------------------------------------------------------------------------

    /**
     * 创建索引
     *
     * @param index    索引名称
     * @param settings settings
     * @param mappings mappings
     * @param aliases  aliases
     * @param payload  payload
     * @param refresh  refresh
     */
    public IndexRes createIndex(
            String index,
            Map<String, Object> settings,
            Map<String, Object> mappings,
            Map<String, Object> aliases,
            Map<String, Object> payload,
            Boolean refresh) throws IOException {
        CreateIndex.Builder builder = new CreateIndex.Builder(index)
                .settings(settings)
                .mappings(mappings)
                .aliases(aliases)
                .payload(payload);
        if (refresh != null) {
            builder.refresh(refresh);
        }
        JestResult jestResult = jestClient.execute(builder.build());
        return new IndexRes(jestResult);
    }

    /**
     * 创建索引
     *
     * @param index    索引名称
     * @param settings settings
     * @param mappings mappings
     * @param aliases  aliases
     */
    public IndexRes createIndex(String index, Map<String, Object> settings, Map<String, Object> mappings, Map<String, Object> aliases) throws IOException {
        return createIndex(index, settings, mappings, aliases, null, null);
    }

    /**
     * 创建索引
     *
     * @param index    索引名称
     * @param settings settings
     * @param mappings mappings
     */
    public IndexRes createIndex(String index, Map<String, Object> settings, Map<String, Object> mappings) throws IOException {
        return createIndex(index, settings, mappings, null, null, null);
    }

    /**
     * 创建索引
     *
     * @param index    索引名称
     * @param settings settings
     */
    public IndexRes createIndex(String index, Map<String, Object> settings) throws IOException {
        return createIndex(index, settings, null, null, null, null);
    }

    /**
     * 删除索引
     *
     * @param index   索引名称
     * @param type    文档类型
     * @param refresh refresh
     */
    public IndexRes deleteIndex(String index, String type, Boolean refresh) throws IOException {
        DeleteIndex.Builder builder = new DeleteIndex.Builder(index);
        if (StringUtils.isNotBlank(type)) {
            builder.type(type);
        }
        if (refresh != null) {
            builder.refresh(refresh);
        }
        JestResult jestResult = jestClient.execute(builder.build());
        return new IndexRes(jestResult);
    }

    /**
     * 删除索引
     *
     * @param index 索引名称
     * @param type  文档类型
     */
    public IndexRes deleteIndex(String index, String type) throws IOException {
        return deleteIndex(index, type, null);
    }

    /**
     * 删除索引
     *
     * @param index 索引名称
     */
    public IndexRes deleteIndex(String index) throws IOException {
        return deleteIndex(index, null, null);
    }

    // --------------------------------------------------------------------------------------------
    // 文档 操作
    // --------------------------------------------------------------------------------------------

    /**
     * 新增或者更新数据
     *
     * @param index   索引名称
     * @param type    文档类型
     * @param id      文档ID
     * @param source  文档数据
     * @param refresh refresh
     */
    public DocRes saveOrUpdate(String index, String type, Object id, Map<String, Object> source, Boolean refresh) throws IOException {
        Index.Builder builder = new Index.Builder(source).index(index).type(type);
        if (id != null) {
            String idStr = String.valueOf(id);
            if (StringUtils.isNotBlank(idStr)) {
                builder.id(idStr);
            }
        }
        if (refresh != null) {
            builder.refresh(refresh);
        }
        DocumentResult documentResult = jestClient.execute(builder.build());
        return new DocRes(documentResult);
    }

    /**
     * 新增或者更新数据
     *
     * @param index  索引名称
     * @param type   文档类型
     * @param id     文档ID
     * @param source 文档数据
     */
    public DocRes saveOrUpdate(String index, String type, Object id, Map<String, Object> source) throws IOException {
        return saveOrUpdate(index, type, id, source, null);
    }

    /**
     * 新增数据
     *
     * @param index  索引名称
     * @param type   文档类型
     * @param source 文档数据
     */
    public DocRes addDoc(String index, String type, Map<String, Object> source) throws IOException {
        return saveOrUpdate(index, type, null, source, null);
    }

    /**
     * 使用update更新数据，文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.8/docs-update.html
     *
     * @param index   索引名称
     * @param type    文档类型
     * @param id      数据id
     * @param payload payload
     * @param refresh refresh
     */
    public DocRes update(String index, String type, Object id, Object payload, Boolean refresh) throws IOException {
        Update.Builder builder = new Update.Builder(payload);
        builder.index(index);
        builder.type(type);
        builder.id(String.valueOf(id));
        if (refresh != null) {
            builder.refresh(refresh);
        }
        DocumentResult documentResult = jestClient.execute(builder.build());
        return new DocRes(documentResult);
    }

    /**
     * 使用update更新数据，文档：https://www.elastic.co/guide/en/elasticsearch/reference/6.8/docs-update.html
     *
     * @param index   索引名称
     * @param type    文档类型
     * @param id      数据id
     * @param payload payload
     */
    public DocRes update(String index, String type, Object id, Object payload) throws IOException {
        return update(index, type, id, payload, null);
    }

    /**
     * 根据查询更新数据 https://www.elastic.co/guide/en/elasticsearch/reference/6.8/docs-update-by-query.html
     *
     * @param indexNames 索引名称集合
     * @param indexTypes 文档类型集合
     * @param payload    payload
     * @param allow      是否允许不定义索引名称
     * @param ignore     忽略不可用的索引，这包括不存在或已关闭的索引
     * @param refresh    refresh
     */
    public UpdateByQueryRes updateByQuery(
            Collection<String> indexNames,
            Collection<String> indexTypes,
            Object payload,
            Boolean allow,
            Boolean ignore,
            Boolean refresh) throws IOException {
        UpdateByQuery.Builder builder = new UpdateByQuery.Builder(payload);
        builder.addIndices(indexNames);
        builder.addTypes(indexTypes);
        if (allow != null) {
            builder.allowNoIndices(allow);
        }
        if (ignore != null) {
            builder.ignoreUnavailable(ignore);
        }
        if (refresh != null) {
            builder.refresh(refresh);
        }
        UpdateByQueryResult updateByQueryResult = jestClient.execute(builder.build());
        return new UpdateByQueryRes(updateByQueryResult);
    }

    /**
     * 根据查询更新数据 https://www.elastic.co/guide/en/elasticsearch/reference/6.8/docs-update-by-query.html
     *
     * @param indexNames 索引名称集合
     * @param indexTypes 文档类型集合
     * @param payload    payload
     * @param refresh    refresh
     */
    public UpdateByQueryRes updateByQuery(Collection<String> indexNames, Collection<String> indexTypes, Object payload, Boolean refresh) throws IOException {
        return updateByQuery(indexNames, indexTypes, payload, null, null, refresh);
    }

    /**
     * 根据查询更新数据 https://www.elastic.co/guide/en/elasticsearch/reference/6.8/docs-update-by-query.html
     *
     * @param indexNames 索引名称集合
     * @param indexTypes 文档类型集合
     * @param payload    payload
     */
    public UpdateByQueryRes updateByQuery(Collection<String> indexNames, Collection<String> indexTypes, Object payload) throws IOException {
        return updateByQuery(indexNames, indexTypes, payload, null, null, null);
    }

    /**
     * 根据查询更新数据 https://www.elastic.co/guide/en/elasticsearch/reference/6.8/docs-update-by-query.html
     *
     * @param index   索引名称集
     * @param type    文档类型集
     * @param payload payload
     * @param allow   是否允许不定义索引名称
     * @param ignore  忽略不可用的索引，这包括不存在或已关闭的索引
     * @param refresh refresh
     */
    public UpdateByQueryRes updateByQuery(String index, String type, Object payload, Boolean allow, Boolean ignore, Boolean refresh) throws IOException {
        UpdateByQuery.Builder builder = new UpdateByQuery.Builder(payload);
        builder.addIndex(index);
        builder.addType(type);
        if (allow != null) {
            builder.allowNoIndices(allow);
        }
        if (ignore != null) {
            builder.ignoreUnavailable(ignore);
        }
        if (refresh != null) {
            builder.refresh(refresh);
        }
        UpdateByQueryResult updateByQueryResult = jestClient.execute(builder.build());
        return new UpdateByQueryRes(updateByQueryResult);
    }

    /**
     * 根据查询更新数据 https://www.elastic.co/guide/en/elasticsearch/reference/6.8/docs-update-by-query.html
     *
     * @param index   索引名称集
     * @param type    文档类型集
     * @param payload payload
     * @param refresh refresh
     */
    public UpdateByQueryRes updateByQuery(String index, String type, Object payload, Boolean refresh) throws IOException {
        return updateByQuery(index, type, payload, null, null, refresh);
    }

    /**
     * 根据查询更新数据 https://www.elastic.co/guide/en/elasticsearch/reference/6.8/docs-update-by-query.html
     *
     * @param index   索引名称集
     * @param type    文档类型集
     * @param payload payload
     */
    public UpdateByQueryRes updateByQuery(String index, String type, Object payload) throws IOException {
        return updateByQuery(index, type, payload, null, null, null);
    }

    /**
     * 根据ID删除数据
     *
     * @param index   索引名称集
     * @param type    文档类型集
     * @param id      数据ID
     * @param refresh refresh
     */
    public DocRes deleteData(String index, String type, Object id, Boolean refresh) throws IOException {
        Delete.Builder builder = new Delete.Builder(String.valueOf(id));
        builder.index(index);
        builder.type(type);
        if (refresh != null) {
            builder.refresh(refresh);
        }
        DocumentResult documentResult = jestClient.execute(builder.build());
        return new DocRes(documentResult);
    }

    /**
     * 根据ID删除数据
     *
     * @param index 索引名称集
     * @param type  文档类型集
     * @param id    数据ID
     */
    public DocRes deleteData(String index, String type, Object id) throws IOException {
        return deleteData(index, type, id, null);
    }

    /**
     * 根据查询删除数据 https://www.elastic.co/guide/en/elasticsearch/reference/6.8/docs-delete-by-query.html
     *
     * @param indexNames 索引名称集合
     * @param indexTypes 文档类型集合
     * @param query      query
     * @param allow      是否允许不定义索引名称
     * @param ignore     忽略不可用的索引，这包括不存在或已关闭的索引
     * @param refresh    refresh
     */
    public DeleteByQueryRes deleteByQuery(
            Collection<String> indexNames,
            Collection<String> indexTypes,
            String query,
            Boolean allow,
            Boolean ignore,
            Boolean refresh) throws IOException {
        DeleteByQuery.Builder builder = new DeleteByQuery.Builder(query);
        builder.addIndices(indexNames);
        builder.addTypes(indexTypes);
        if (allow != null) {
            builder.allowNoIndices(allow);
        }
        if (ignore != null) {
            builder.ignoreUnavailable(ignore);
        }
        if (refresh != null) {
            builder.refresh(refresh);
        }
        JestResult jestResult = jestClient.execute(builder.build());
        return new DeleteByQueryRes(jestResult);
    }

    /**
     * 根据查询删除数据 https://www.elastic.co/guide/en/elasticsearch/reference/6.8/docs-delete-by-query.html
     *
     * @param indexNames 索引名称集合
     * @param indexTypes 文档类型集合
     * @param query      query
     * @param refresh    refresh
     */
    public DeleteByQueryRes deleteByQuery(Collection<String> indexNames, Collection<String> indexTypes, String query, Boolean refresh) throws IOException {
        return deleteByQuery(indexNames, indexTypes, query, null, null, refresh);
    }

    /**
     * 根据查询删除数据 https://www.elastic.co/guide/en/elasticsearch/reference/6.8/docs-delete-by-query.html
     *
     * @param indexNames 索引名称集合
     * @param indexTypes 文档类型集合
     * @param query      query
     */
    public DeleteByQueryRes deleteByQuery(Collection<String> indexNames, Collection<String> indexTypes, String query) throws IOException {
        return deleteByQuery(indexNames, indexTypes, query, null, null, null);
    }

    /**
     * 根据查询删除数据 https://www.elastic.co/guide/en/elasticsearch/reference/6.8/docs-delete-by-query.html
     *
     * @param index   索引名称
     * @param type    文档类型
     * @param query   query
     * @param allow   是否允许不定义索引名称
     * @param ignore  忽略不可用的索引，这包括不存在或已关闭的索引
     * @param refresh refresh
     */
    public DeleteByQueryRes deleteByQuery(String index, String type, String query, Boolean allow, Boolean ignore, Boolean refresh) throws IOException {
        DeleteByQuery.Builder builder = new DeleteByQuery.Builder(query);
        builder.addIndex(index);
        builder.addType(type);
        if (allow != null) {
            builder.allowNoIndices(allow);
        }
        if (ignore != null) {
            builder.ignoreUnavailable(ignore);
        }
        if (refresh != null) {
            builder.refresh(refresh);
        }
        JestResult jestResult = jestClient.execute(builder.build());
        return new DeleteByQueryRes(jestResult);
    }

    /**
     * 根据查询删除数据 https://www.elastic.co/guide/en/elasticsearch/reference/6.8/docs-delete-by-query.html
     *
     * @param index   索引名称
     * @param type    文档类型
     * @param query   query
     * @param refresh refresh
     */
    public DeleteByQueryRes deleteByQuery(String index, String type, String query, Boolean refresh) throws IOException {
        return deleteByQuery(index, type, query, null, null, refresh);
    }

    /**
     * 根据查询删除数据 https://www.elastic.co/guide/en/elasticsearch/reference/6.8/docs-delete-by-query.html
     *
     * @param index 索引名称
     * @param type  文档类型
     * @param query query
     */
    public DeleteByQueryRes deleteByQuery(String index, String type, String query) throws IOException {
        return deleteByQuery(index, type, query, null, null, null);
    }

    // --------------------------------------------------------------------------------------------
    // 搜索 操作
    // --------------------------------------------------------------------------------------------

    /**
     * 搜索查询
     *
     * @param indexNames        索引名称集合
     * @param indexTypes        文档类型集合
     * @param query             query
     * @param includePattern    includePattern
     * @param excludePattern    excludePattern
     * @param sorts             sorts --> [ {field: 'fieldName', order: 'ASC/DESC'}, ...]
     * @param enableTrackScores enableTrackScores
     * @param allow             是否允许不定义索引名称
     * @param ignore            忽略不可用的索引，这包括不存在或已关闭的索引
     * @param refresh           refresh
     */
    public SearchRes search(
            Collection<String> indexNames,
            Collection<String> indexTypes,
            String query,
            String includePattern,
            String excludePattern,
            Collection<Map<String, String>> sorts,
            Boolean enableTrackScores,
            Boolean allow,
            Boolean ignore,
            Boolean refresh) throws IOException {
        Search.Builder builder = new Search.Builder(query);
        builder.addIndices(indexNames);
        builder.addTypes(indexTypes);
        // builder.setSearchType()
        if (StringUtils.isNotBlank(includePattern)) {
            builder.addSourceIncludePattern(includePattern);
        }
        if (StringUtils.isNotBlank(excludePattern)) {
            builder.addSourceExcludePattern(excludePattern);
        }
        if (enableTrackScores != null) {
            builder.enableTrackScores();
        }
        if (sorts != null && !sorts.isEmpty()) {
            builder.addSort(mapsToSorts(sorts));
        }
        if (allow != null) {
            builder.allowNoIndices(allow);
        }
        if (ignore != null) {
            builder.ignoreUnavailable(ignore);
        }
        if (refresh != null) {
            builder.refresh(refresh);
        }
        SearchResult searchResult = jestClient.execute(builder.build());
        return new SearchRes(searchResult);
    }

    /**
     * 搜索查询
     *
     * @param indexNames     索引名称集合
     * @param indexTypes     文档类型集合
     * @param query          query
     * @param includePattern includePattern
     * @param excludePattern excludePattern
     * @param sorts          sorts --> [ {field: 'fieldName', order: 'ASC/DESC'}, ...]
     */
    public SearchRes search(
            Collection<String> indexNames,
            Collection<String> indexTypes,
            String query,
            String includePattern,
            String excludePattern,
            Collection<Map<String, String>> sorts) throws IOException {
        return search(indexNames, indexTypes, query, includePattern, excludePattern, sorts, null, null, null, null);
    }

    /**
     * 搜索查询
     *
     * @param indexNames 索引名称集合
     * @param indexTypes 文档类型集合
     * @param query      query
     * @param sorts      sorts --> [ {field: 'fieldName', order: 'ASC/DESC'}, ...]
     */
    public SearchRes search(
            Collection<String> indexNames,
            Collection<String> indexTypes,
            String query,
            Collection<Map<String, String>> sorts) throws IOException {
        return search(indexNames, indexTypes, query, null, null, sorts, null, null, null, null);
    }

    // --------------------------------------------------------------------------------------------
    // 获取文档 操作
    // --------------------------------------------------------------------------------------------

    /**
     * 根据ID获取数据
     *
     * @param index   索引名称
     * @param id      数据ID
     * @param refresh refresh
     */
    public DocRes getData(String index, Object id, Boolean refresh) throws IOException {
        Get.Builder builder = new Get.Builder(index, String.valueOf(id));
        if (refresh != null) {
            builder.refresh(refresh);
        }
        DocumentResult documentResult = jestClient.execute(builder.build());
        return new DocRes(documentResult);
    }

    /**
     * 根据ID获取数据
     *
     * @param index 索引名称
     * @param id    数据ID
     */
    public DocRes getData(String index, Object id) throws IOException {
        return getData(index, id, null);
    }

    /**
     * 根据ID集合获取数据
     *
     * @param index   索引名称
     * @param type    文档类型
     * @param ids     数据ID集合
     * @param refresh refresh
     */
    public MultiGetRes multiGet(String index, String type, Collection<?> ids, Boolean refresh) throws IOException {
        MultiGet.Builder.ById builder = new MultiGet.Builder.ById(index, type);
        builder.addId(toStrList(ids));
        if (refresh != null) {
            builder.refresh(refresh);
        }
        JestResult jestResult = jestClient.execute(builder.build());
        return new MultiGetRes(jestResult);
    }

    /**
     * 根据ID集合获取数据
     *
     * @param index 索引名称
     * @param type  文档类型
     * @param ids   数据ID集合
     */
    public MultiGetRes multiGet(String index, String type, Collection<Object> ids) throws IOException {
        return multiGet(index, type, ids, null);
    }

    // TODO 更多API支持

    // --------------------------------------------------------------------------------------------
    // 其他 操作
    // --------------------------------------------------------------------------------------------

    private List<Sort> mapsToSorts(Collection<Map<String, String>> sorts) {
        if (sorts == null) {
            return null;
        }
        return sorts.stream().map(this::mapToSort).collect(Collectors.toList());
    }

    private Sort mapToSort(Map<String, String> sort) {
        if (sort == null) {
            return null;
        }
        String field = sort.get("field");
        String order = sort.get("order");
        Sort.Sorting sorting = null;
        if (order.equalsIgnoreCase("ASC")) {
            sorting = Sort.Sorting.ASC;
        } else if (order.equalsIgnoreCase("DESC")) {
            sorting = Sort.Sorting.DESC;
        }
        Sort result;
        if (sorting == null) {
            result = new Sort(field);
        } else {
            result = new Sort(field, sorting);
        }
        return result;
    }

    public List<String> toStrList(Collection<?> ids) {
        if (ids == null) {
            return null;
        }
        return ids.stream().map(String::valueOf).collect(Collectors.toList());
    }
}
