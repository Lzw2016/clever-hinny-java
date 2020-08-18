package org.clever.hinny.core;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.clever.common.utils.excel.ExcelDataReader;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/08/18 21:12 <br/>
 */
@Slf4j
public class ExcelUtilsTest {

    private final String file = "C:\\Users\\lizw\\Downloads\\药店积分商品统计20200813141849.xlsx";

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
}
