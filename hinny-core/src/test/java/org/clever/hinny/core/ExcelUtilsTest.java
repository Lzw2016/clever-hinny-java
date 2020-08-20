package org.clever.hinny.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.clever.common.utils.excel.ExcelDataReader;
import org.clever.common.utils.excel.ExcelDataWriter;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/08/18 21:12 <br/>
 */
@Slf4j
public class ExcelUtilsTest {

    private final String file = "C:\\Users\\lizw\\Downloads\\药店积分商品统计20200813141849.xlsx";
    private final String file2 = "C:\\Users\\lizw\\Downloads\\药店积分商品统计20200813141850.xlsx";

    @Test
    public void t01() {
        log.info("-> {}", Byte[].class.getName());
        log.info("-> {}", Byte.class.getName());

        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("aaa", "111");
        map.put("bbb", "222");
        map.put("ccc", "333");
        map.put("ddd", "444");
        map.put("eee", "555");
        map.put("fff", "666");
        log.info(" -> {}", map.values());
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void t02() throws IOException {
        ExcelUtils.ExcelDataReaderConfig config = new ExcelUtils.ExcelDataReaderConfig();
        config.setFilename(file);
        config.setInputStream(FileUtils.openInputStream(new File(file)));
        config.getColumns().put("aaa", new ExcelUtils.ExcelReaderHeadConfig(Integer.class, "序号"));
        config.getColumns().put("bbb", new ExcelUtils.ExcelReaderHeadConfig(Long.class, "药店ID"));
        config.getColumns().put("ccc", new ExcelUtils.ExcelReaderHeadConfig(String.class, "药店名称"));
        config.getColumns().put("ddd", new ExcelUtils.ExcelReaderHeadConfig(BigDecimal.class, "积分商品总数量"));
        config.getColumns().put("eee", new ExcelUtils.ExcelReaderHeadConfig(BigDecimal.class, "上架积分商品数"));
        config.getColumns().put("fff", new ExcelUtils.ExcelReaderHeadConfig(BigDecimal.class, "下架积分商品数"));

        ExcelDataReader<Map> reader = ExcelUtils.Instance.createReader(config);
        reader.read().sheet(0).doRead();
        log.info("data -> {}", reader.getExcelData(0));
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void t03() throws IOException {
        ExcelUtils.ExcelDataReaderConfig config = new ExcelUtils.ExcelDataReaderConfig();
        config.setFilename(file);
        config.setInputStream(FileUtils.openInputStream(new File(file)));
        config.getColumns().put("aaa", new ExcelUtils.ExcelReaderHeadConfig(Integer.class, "第一", "序号"));
        config.getColumns().put("bbb", new ExcelUtils.ExcelReaderHeadConfig(Long.class, "第一", "药店ID"));
        config.getColumns().put("ccc", new ExcelUtils.ExcelReaderHeadConfig(String.class, "第一", "药店名称"));
        config.getColumns().put("ddd", new ExcelUtils.ExcelReaderHeadConfig(BigDecimal.class, "第二", "积分商品总数量"));
        config.getColumns().put("eee", new ExcelUtils.ExcelReaderHeadConfig(Double.class, "第二", "上架积分商品数"));
        config.getColumns().put("fff", new ExcelUtils.ExcelReaderHeadConfig(Float.class, "第二", "下架积分商品数"));

        ExcelDataReader<Map> reader = ExcelUtils.Instance.createReader(config);
        reader.read().headRowNumber(1).doReadAll();
        log.info("data -> {}", reader.getExcelData(0));
    }

    @Test
    public void t04() {
        List<List<Object>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            List<Object> data = new ArrayList<>();
            data.add("字符串" + i);
            data.add(new Date());
            data.add(0.56);
            data.add("111");
            data.add("222");
            data.add("333");
            data.add("444");
            list.add(data);
        }
        ExcelUtils.ExcelDataWriterConfig config = new ExcelUtils.ExcelDataWriterConfig();
        config.setFileName(file2);
        ExcelUtils.ExcelWriterHeadConfig headConfig = new ExcelUtils.ExcelWriterHeadConfig("第一", "序号");
        headConfig.getColumnWidth().setColumnWidth(10);
        headConfig.getHeadFontStyle().setBold(false);
        config.getColumns().put("aaa", headConfig);
        headConfig = new ExcelUtils.ExcelWriterHeadConfig("第一", "药店ID");
        headConfig.getColumnWidth().setColumnWidth(20);
        config.getColumns().put("bbb", headConfig);
        headConfig = new ExcelUtils.ExcelWriterHeadConfig("第一", "药店名称");
        headConfig.getColumnWidth().setColumnWidth(12);
        config.getColumns().put("ccc", headConfig);
        headConfig = new ExcelUtils.ExcelWriterHeadConfig("第二", "积分商品总数量");
        headConfig.getColumnWidth().setColumnWidth(18);
        config.getColumns().put("ddd", headConfig);
        headConfig = new ExcelUtils.ExcelWriterHeadConfig("第二", "上架积分商品数");
        headConfig.getColumnWidth().setColumnWidth(18);
        config.getColumns().put("eee", headConfig);
        headConfig = new ExcelUtils.ExcelWriterHeadConfig("第二", "下架积分商品数");
        headConfig.getColumnWidth().setColumnWidth(18);
        config.getColumns().put("fff", headConfig);
        ExcelDataWriter writer = ExcelUtils.Instance.createWriter(config);
        writer.write().sheet("test").doWrite(list);
    }

    @Test
    public void t05() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("序号", "字符串" + i);
            data.put("药店ID", new Date());
            data.put("药店名称", 0.56);
            data.put("积分商品总数量", "111");
            data.put("上架积分商品数", "222");
            data.put("下架积分商品数", "333");
            data.put("测试", "444");
            list.add(data);
        }
        ExcelUtils.ExcelDataWriterConfig config = new ExcelUtils.ExcelDataWriterConfig();
        config.setFileName(file2);
        config.getColumns().put("aaa", new ExcelUtils.ExcelWriterHeadConfig("序号"));
        config.getColumns().put("bbb", new ExcelUtils.ExcelWriterHeadConfig("药店ID"));
        config.getColumns().put("ccc", new ExcelUtils.ExcelWriterHeadConfig("药店名称"));
        config.getColumns().put("ddd", new ExcelUtils.ExcelWriterHeadConfig("积分商品总数量"));
        config.getColumns().put("eee", new ExcelUtils.ExcelWriterHeadConfig("上架积分商品数"));
        config.getColumns().put("fff", new ExcelUtils.ExcelWriterHeadConfig("下架积分商品数"));
        ExcelDataWriter writer = ExcelUtils.Instance.createWriter(config);
        writer.write().sheet("test").doWrite(list);
    }
}
