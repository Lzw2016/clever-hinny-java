package org.clever.hinny.freemarker;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/10/04 22:17 <br/>
 */
@org.springframework.context.annotation.Configuration
public class FreeMarkerConfigurer {
    /**
     * FreeMarker容器
     */
    private static Configuration CONFIG;

    public static Configuration getConfig() {
        return CONFIG;
    }

    @Bean
    public Configuration configuration(ObjectProvider<Configuration> configuration) {
        try {
            CONFIG = configuration.getIfAvailable();
        } catch (Exception e) {
            CONFIG = createConfiguration();
        }
        return CONFIG;
    }

    private static Configuration createConfiguration() {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_30);
        StringTemplateLoader stringLoader = new StringTemplateLoader();
        configuration.setTemplateLoader(stringLoader);
        // FreeMarker 模版数据null值处理，不抛异常
        configuration.setClassicCompatible(true);
        return configuration;
    }
}
