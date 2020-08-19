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
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void t02() throws IOException {
        ExcelUtils.ExcelDataReaderConfig config = new ExcelUtils.ExcelDataReaderConfig();
        config.setFilename(file);
        config.setInputStream(FileUtils.openInputStream(new File(file)));
        config.getColumns().put("序号", Integer.class);
        config.getColumns().put("药店ID", Long.class);
        config.getColumns().put("药店名称", String.class);
        config.getColumns().put("积分商品总数量", BigDecimal.class);
        config.getColumns().put("上架积分商品数", BigDecimal.class);
        config.getColumns().put("下架积分商品数", BigDecimal.class);

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
        config.getColumns().put("序号", Integer.class);
        config.getColumns().put("药店ID", Long.class);
        config.getColumns().put("药店名称", String.class);
        config.getColumns().put("积分商品总数量", BigDecimal.class);
        config.getColumns().put("上架积分商品数", Long.class);
        config.getColumns().put("下架积分商品数", Double.class);

        ExcelDataReader<Map> reader = ExcelUtils.Instance.createReader(config);
        reader.read().doReadAll();
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
        config.getColumns().add(new ExcelUtils.HeadConfig("第一", "序号"));
        config.getColumns().add(new ExcelUtils.HeadConfig("第一", "药店ID"));
        config.getColumns().add(new ExcelUtils.HeadConfig("第一", "药店名称"));
        config.getColumns().add(new ExcelUtils.HeadConfig("第二", "积分商品总数量"));
        config.getColumns().add(new ExcelUtils.HeadConfig("第二", "上架积分商品数"));
        config.getColumns().add(new ExcelUtils.HeadConfig("第二", "下架积分商品数"));
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
        config.getColumns().add(new ExcelUtils.HeadConfig("序号"));
        config.getColumns().add(new ExcelUtils.HeadConfig("药店ID"));
        config.getColumns().add(new ExcelUtils.HeadConfig("药店名称"));
        config.getColumns().add(new ExcelUtils.HeadConfig("积分商品总数量"));
        config.getColumns().add(new ExcelUtils.HeadConfig("上架积分商品数"));
        config.getColumns().add(new ExcelUtils.HeadConfig("下架积分商品数"));
        ExcelDataWriter writer = ExcelUtils.Instance.createWriter(config);
        writer.write().sheet("test").doWrite(list);
    }
}
