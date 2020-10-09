package org.clever.hinny.freemarker;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ymx
 * @version 1.0
 * @date 2020/10/9 16:07
 */
@Slf4j
public class Test01 {

    @Test
    public void t01() {
        try {
            String result = "";
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);
            cfg.setNumberFormat("0.##");
            cfg.setClassicCompatible(true);
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("name", "Brady");
            paramMap.put("age", "18岁");
            paramMap.put("high", "180cm");
            StringTemplateLoader stringLoader = new StringTemplateLoader();
            String templateContent = "${name} /${age} /${high}";
            stringLoader.putTemplate("myTemplate", templateContent);
            cfg.setTemplateLoader(stringLoader);
            Template template = cfg.getTemplate("myTemplate", FreeMarkerUtils.Default_Encoding);
            Writer writer = new StringWriter();
            template.process(paramMap, writer);
            result = writer.toString();
            writer.flush();
            log.info("res=========>{}", result);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }
}
