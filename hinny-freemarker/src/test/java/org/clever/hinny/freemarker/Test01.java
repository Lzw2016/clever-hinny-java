package org.clever.hinny.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
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
        try (Writer writer = new StringWriter()) {
            Configuration cfg = new Configuration(Configuration.VERSION_2_3_30);
            cfg.setNumberFormat("0.##");
            cfg.setClassicCompatible(true);
            cfg.setEncoding(Locale.CHINA, "utf-8");
            cfg.setClassForTemplateLoading(this.getClass(), "/templates/");
            Template template = cfg.getTemplate("msg.ftl", FreeMarkerUtils.Default_Encoding);
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("url", "entry.getKey()");
            paramMap.put("MM", "1211112313");
            paramMap.put("date", "2上次那家超时订单");
            paramMap.put("subject", "ssl证书过期提醒");
            template.process(paramMap, writer);
            log.info("res=========>\n{}", writer.toString());
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }
    }
}
