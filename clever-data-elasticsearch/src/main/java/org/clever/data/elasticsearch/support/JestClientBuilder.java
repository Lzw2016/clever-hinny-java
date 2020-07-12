package org.clever.data.elasticsearch.support;

import com.google.gson.Gson;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.springframework.boot.autoconfigure.elasticsearch.jest.HttpClientConfigBuilderCustomizer;
import org.springframework.boot.autoconfigure.elasticsearch.jest.JestProperties;
import org.springframework.util.Assert;

import java.util.List;

/**
 * 参考 org.springframework.boot.autoconfigure.elasticsearch.jest.JestAutoConfiguration
 * <p>
 * 作者：lizw <br/>
 * 创建时间：2019/10/23 16:08 <br/>
 */
public class JestClientBuilder {
    /**
     * 默认的JSON序列化/反序列化Gson实现
     */
    private static final Gson Default_Gson = new Gson();

    private final JestProperties properties;
    private final Gson gson;
    private final List<HttpClientConfigBuilderCustomizer> builderCustomizers;
    @Getter
    private final JestClient jestClient;

    /**
     * @param properties         ElasticSearch配置
     * @param gson               JSON序列化/反序列化Gson对象
     * @param builderCustomizers 自定义配置
     */
    public JestClientBuilder(JestProperties properties, Gson gson, List<HttpClientConfigBuilderCustomizer> builderCustomizers) {
        Assert.notNull(properties, "JestProperties不能为空");
        this.properties = properties;
        this.gson = gson == null ? Default_Gson : gson;
        this.builderCustomizers = builderCustomizers;
        this.jestClient = initJestClient();
    }

    /**
     * @param properties ElasticSearch配置
     * @param gson       JSON序列化/反序列化Gson对象
     */
    public JestClientBuilder(JestProperties properties, Gson gson) {
        this(properties, gson, null);
    }

    /**
     * @param properties ElasticSearch配置
     */
    public JestClientBuilder(JestProperties properties) {
        this(properties, null, null);
    }

    public JestClientBuilder(JestClientFactory jestClientFactory) {
        Assert.notNull(jestClientFactory, "JestClientFactory不能为空");
        this.properties = null;
        this.gson = Default_Gson;
        this.builderCustomizers = null;
        this.jestClient = jestClientFactory.getObject();
    }

    public JestClientBuilder(JestClient jestClient) {
        Assert.notNull(jestClient, "JestClient不能为空");
        this.properties = null;
        this.gson = Default_Gson;
        this.builderCustomizers = null;
        this.jestClient = jestClient;
    }

    private JestClient initJestClient() {
        if (jestClient != null) {
            return jestClient;
        }
        JestClientFactory factory = new JestClientFactory();
        factory.setHttpClientConfig(createHttpClientConfig());
        return factory.getObject();
    }

    private HttpClientConfig createHttpClientConfig() {
        HttpClientConfig.Builder builder = new HttpClientConfig.Builder(properties.getUris());
        if (StringUtils.isNotBlank(properties.getUsername())) {
            builder.defaultCredentials(properties.getUsername(), properties.getPassword());
        }
        JestProperties.Proxy proxy = this.properties.getProxy();
        if (StringUtils.isNotBlank(proxy.getHost())) {
            Assert.notNull(proxy.getPort(), "Proxy port must not be null");
            builder.proxy(new HttpHost(proxy.getHost(), proxy.getPort()));
        }
        if (gson != null) {
            builder.gson(gson);
        }
        if (properties.isMultiThreaded()) {
            builder.multiThreaded(true);
        }
        if (properties.getConnectionTimeout() != null) {
            builder.connTimeout((int) properties.getConnectionTimeout().toMillis());
        }
        if (properties.getReadTimeout() != null) {
            builder.readTimeout((int) properties.getReadTimeout().toMillis());
        }
        customize(builder);
        return builder.build();
    }

    private void customize(HttpClientConfig.Builder builder) {
        if (builderCustomizers == null) {
            return;
        }
        builderCustomizers.forEach((customizer) -> customizer.customize(builder));
    }
}
