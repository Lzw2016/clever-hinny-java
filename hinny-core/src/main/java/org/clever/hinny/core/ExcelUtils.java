package org.clever.hinny.core;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.utils.excel.ExcelDataReader;
import org.clever.common.utils.excel.ExcelDataWriter;
import org.clever.common.utils.excel.ExcelReaderExceptionHand;
import org.clever.common.utils.excel.ExcelRowReader;
import org.clever.common.utils.excel.dto.ExcelData;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/07/28 22:33 <br/>
 */
public class ExcelUtils {
    public static final ExcelUtils Instance = new ExcelUtils();

    private ExcelUtils() {
    }

    @SuppressWarnings("rawtypes")
    public ExcelDataReader<Map> createReader(ExcelDataReaderConfig config) {
        Assert.notNull(config, "参数config不能为null");
        ExcelDataReader<Map> excelDataReader = new ExcelDataReader<>(
                config.filename,
                config.inputStream,
                Map.class,
                config.limitRows,
                config.enableExcelData,
                config.excelRowReader,
                config.excelReaderExceptionHand);
        excelDataReader.setEnableValidation(false);
        ExcelReaderBuilder builder = excelDataReader.read();
        builder.autoCloseStream(config.autoCloseStream);
        if (config.extraRead != null) {
            for (CellExtraTypeEnum typeEnum : config.extraRead) {
                if (typeEnum != null) {
                    builder.extraRead(typeEnum);
                }
            }
        }
        builder.ignoreEmptyRow(config.ignoreEmptyRow);
        builder.mandatoryUseInputStream(config.mandatoryUseInputStream);
        if (config.password != null) {
            builder.password(config.password);
        }
        if (StringUtils.isNotBlank(config.sheetName)) {
            builder.sheet(config.sheetName);
        }
        if (config.sheetNo != null) {
            builder.sheet(config.sheetNo);
        }
        builder.headRowNumber(config.headRowNumber);
        builder.useScientificFormat(config.useScientificFormat);
        builder.use1904windowing(config.use1904windowing);
        if (config.locale != null) {
            builder.locale(config.locale);
        }
        builder.autoTrim(config.autoTrim);
        builder.customObject(config.customObject);
        // 自定义解析逻辑
        builder.useDefaultListener(false);
        builder.registerReadListener(new ExcelDateReadListener(config, excelDataReader));
        return excelDataReader;
    }

    public ExcelDataWriter createWriter(ExcelDataWriterConfig config) {
        return null;
    }

    @Data
    public static class ExcelDataReaderConfig implements Serializable {
        /**
         * 上传的Excel文件名称
         */
        private String filename;
        /**
         * 上传的文件数据流
         */
        private InputStream inputStream;
        /**
         * 读取Excel文件最大行数
         */
        private int limitRows;
        /**
         * 是否缓存读取的数据结果到内存中(默认启用)
         */
        private boolean enableExcelData;
        /**
         * 是否启用数据校验(默认启用)
         */
        private boolean enableValidation = true;
        /**
         * 处理读取Excel异常
         */
        private ExcelReaderExceptionHand excelReaderExceptionHand;
        /**
         * 处理Excel数据行
         */
        @SuppressWarnings("rawtypes")
        private ExcelRowReader<Map> excelRowReader;

        // ----------------------------------------------------------------------

        /**
         * 是否自动关闭输入流
         */
        private boolean autoCloseStream = false;

        /**
         * 读取扩展信息配置
         */
        private CellExtraTypeEnum[] extraRead = new CellExtraTypeEnum[]{};

        /**
         * 是否忽略空行
         */
        private boolean ignoreEmptyRow = false;

        /**
         * 强制使用输入流，如果为false，则将“inputStream”传输到临时文件以提高效率
         */
        private boolean mandatoryUseInputStream = false;

        /**
         * Excel文件密码
         */
        private String password;

        /**
         * Excel页签编号(从0开始)
         */
        private Integer sheetNo;

        /**
         * Excel页签名称(xlsx格式才支持)
         */
        private String sheetName;

        /**
         * 表头行数
         */
        private int headRowNumber = 1;

        /**
         * 使用科学格式
         */
        private boolean useScientificFormat = false;

        /**
         * 如果日期使用1904窗口，则为True；如果使用1900日期窗口，则为false
         */
        private boolean use1904windowing = false;

        /**
         * Locale对象表示特定的地理、政治或文化区域。设置日期和数字格式时使用此参数
         */
        private Locale locale = Locale.SIMPLIFIED_CHINESE;

        /**
         * 自动删除空格字符
         */
        private boolean autoTrim = true;

        /**
         * 设置一个自定义对象，可以在侦听器中读取此对象(AnalysisContext.getCustom())
         */
        private Object customObject;

        /**
         * Excel列配置(表头)
         */
        private final Map<String, Class<?>> columns = new HashMap<>();
    }

//    JString = "JString",
//    JBigDecimal = "JBigDecimal",
//    JBoolean = "JBoolean",
//    JDate = "JDate",
//    JInteger = "JInteger",
//    JDouble = "JDouble",
//    JLong = "JLong",
//    JFloat = "JFloat",
//    JShort = "JShort",
//    JByte = "JByte",
//    JByteArray = "JByte[]",

    @Data
    public static class ExcelDataWriterConfig implements Serializable {

    }

    @SuppressWarnings("rawtypes")
    private static class ExcelDateReadListener extends AnalysisEventListener<Map<Integer, CellData<?>>> {
        private final ExcelDataReaderConfig config;
        private final ExcelDataReader<Map> excelDataReader;
        private final Map<String, Class<?>> columns = new HashMap<>();

        public ExcelDateReadListener(ExcelDataReaderConfig config, ExcelDataReader<Map> excelDataReader) {
            Assert.notNull(config, "参数config不能为null");
            Assert.notNull(excelDataReader, "参数excelDataReader不能为null");
            this.config = config;
            this.excelDataReader = excelDataReader;
        }

        private ExcelData<Map> getExcelData(AnalysisContext context) {
            final Integer sheetNo = context.readSheetHolder().getSheetNo();
            final String sheetName = context.readSheetHolder().getSheetName();
            String key = String.format("%s-%s", sheetNo, sheetName);
            return excelDataReader.getExcelSheetMap().computeIfAbsent(key, s -> new ExcelData<>(Map.class, sheetName, sheetNo));
        }

        @Override
        public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
            ExcelData<Map> excelData = getExcelData(context);
            if (excelData.getStartTime() == null) {
                excelData.setStartTime(System.currentTimeMillis());
            }
            Map<String, Class<?>> columnsConfig = config.getColumns();
            for (Map.Entry<Integer, String> entry : headMap.entrySet()) {
                Integer index = entry.getKey();
                String head = entry.getValue();
                Class<?> clazz = null;
                if (columnsConfig == null || columnsConfig.isEmpty()) {

                } else {
                    clazz = columnsConfig.get(head);
                    if (clazz == null) {
                        continue;
                    }
                }
                columns.put(head, clazz);
            }

        }

        @Override
        public void invoke(Map<Integer, CellData<?>> data, AnalysisContext context) {

        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {

        }

        @Override
        public void onException(Exception exception, AnalysisContext context) throws Exception {

        }

        @Override
        public boolean hasNext(AnalysisContext context) {
            return true;
        }
    }

//    public static class ConverterUtils {
//        private ConverterUtils() {
//        }
//
//        public static Object convertToJavaObject(
//                CellData<?> cellData,
//                Field field,
//                ExcelContentProperty contentProperty,
//                Map<String, Converter<?>> converterMap,
//                GlobalConfiguration globalConfiguration,
//                Integer rowIndex,
//                Integer columnIndex) {
//
//        }
//
//        private static Object doConvertToJavaObject(
//                CellData<?> cellData,
//                Class<?> clazz,
//                ExcelContentProperty contentProperty,
//                Map<String, Converter<?>> converterMap,
//                GlobalConfiguration globalConfiguration,
//                Integer rowIndex,
//                Integer columnIndex) {
//
//        }
//    }
}
