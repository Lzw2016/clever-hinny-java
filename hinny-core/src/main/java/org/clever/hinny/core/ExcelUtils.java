package org.clever.hinny.core;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ConverterKeyBuild;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.enums.CellExtraTypeEnum;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.event.NotRepeatExecutor;
import com.alibaba.excel.exception.ExcelDataConvertException;
import com.alibaba.excel.metadata.Cell;
import com.alibaba.excel.metadata.CellData;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.Head;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.metadata.holder.ReadHolder;
import com.alibaba.excel.read.metadata.property.ExcelReadHeadProperty;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.handler.AbstractCellWriteHandler;
import com.alibaba.excel.write.metadata.holder.WriteSheetHolder;
import com.alibaba.excel.write.metadata.holder.WriteTableHolder;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.clever.common.utils.codec.DigestUtils;
import org.clever.common.utils.codec.EncodeDecodeUtils;
import org.clever.common.utils.excel.ExcelDataReader;
import org.clever.common.utils.excel.ExcelDataWriter;
import org.clever.common.utils.excel.ExcelReaderExceptionHand;
import org.clever.common.utils.excel.ExcelRowReader;
import org.clever.common.utils.excel.dto.ExcelData;
import org.clever.common.utils.excel.dto.ExcelRow;
import org.clever.common.utils.tuples.TupleTow;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 作者：lizw <br/>
 * 创建时间：2020/07/28 22:33 <br/>
 */
public class ExcelUtils {
    public static final ExcelUtils Instance = new ExcelUtils();

    private ExcelUtils() {
    }

    @SuppressWarnings("rawtypes")
    @SneakyThrows
    public ExcelDataReader<Map> createReader(ExcelDataReaderConfig config) {
        Assert.notNull(config, "参数config不能为null");
        ExcelDataReader<Map> excelDataReader;
        if (config.getRequest() != null) {
            excelDataReader = new ExcelDataReader<>(
                    config.getRequest(),
                    Map.class,
                    config.limitRows,
                    config.enableExcelData,
                    false,
                    config.excelRowReader,
                    config.excelReaderExceptionHand);
        } else {
            excelDataReader = new ExcelDataReader<>(
                    config.filename,
                    config.inputStream,
                    Map.class,
                    config.limitRows,
                    config.enableExcelData,
                    false,
                    config.excelRowReader,
                    config.excelReaderExceptionHand);
        }
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
        Assert.notNull(config, "参数config不能为null");
        ExcelDataWriter excelDataWriter;
        if (config.request != null && config.response != null) {
            excelDataWriter = new ExcelDataWriter(config.request, config.response, config.fileName, null);
        } else {
            excelDataWriter = new ExcelDataWriter(config.outputStream, null);
            if (StringUtils.isNotBlank(config.fileName)) {
                excelDataWriter.write().file(config.fileName);
            }
        }
        ExcelWriterBuilder builder = excelDataWriter.write();
        List<List<String>> heads = config.getHeads();
        if (heads.isEmpty() || heads.get(0).isEmpty()) {
            builder.needHead(false);
        } else {
            builder.head(heads);
        }
        builder.autoCloseStream(config.autoCloseStream);
        builder.inMemory(config.inMemory);
        if (StringUtils.isNotBlank(config.template)) {
            builder.withTemplate(config.template);
        }
        if (config.templateInputStream != null) {
            builder.withTemplate(config.templateInputStream);
        }
        builder.automaticMergeHead(config.automaticMergeHead);
        if (!config.excludeColumnFiledNames.isEmpty()) {
            builder.excludeColumnFiledNames(config.excludeColumnFiledNames);
        }
        if (!config.excludeColumnIndexes.isEmpty()) {
            builder.excludeColumnIndexes(config.excludeColumnIndexes);
        }
        if (!config.includeColumnFiledNames.isEmpty()) {
            builder.includeColumnFiledNames(config.includeColumnFiledNames);
        }
        if (!config.includeColumnIndexes.isEmpty()) {
            builder.includeColumnIndexes(config.includeColumnIndexes);
        }
        builder.needHead(config.needHead);
        builder.relativeHeadRowIndex(config.relativeHeadRowIndex);
        builder.useDefaultStyle(config.useDefaultStyle);
        builder.excelType(config.excelType);
        if (config.password != null) {
            builder.password(config.password);
        }
        if (config.sheetNo != null) {
            builder.sheet(config.sheetNo);
        }
        if (StringUtils.isNotBlank(config.sheetName)) {
            builder.sheet(config.sheetName);
        }
        builder.use1904windowing(config.use1904windowing);
        if (config.locale != null) {
            builder.locale(config.locale);
        }
        builder.autoTrim(config.autoTrim);
        // TODO 根据列配置加入各种 WriteHandler 如：AbstractHeadColumnWidthStyleStrategy、AbstractVerticalCellStyleStrategy。参考 AbstractWriteHolder
        builder.registerWriteHandler(new ColumnStyleStrategy(config));
        return excelDataWriter;
    }

    @Data
    public static class ExcelDataReaderConfig implements Serializable {
        /**
         * Excel文件上传的请求对象
         */
        private HttpServletRequest request;
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
        private int limitRows = org.clever.common.utils.excel.ExcelDataReader.LIMIT_ROWS;
        /**
         * 是否缓存读取的数据结果到内存中(默认启用)
         */
        private boolean enableExcelData = true;
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

    @Data
    public static class ExcelDataWriterConfig implements Serializable {
        /**
         * Excel导出请求对象
         */
        private HttpServletRequest request;
        /**
         * Excel导出响应对象
         */
        private HttpServletResponse response;
        /**
         * Excel导出文件名
         */
        private String fileName;
        /**
         * Excel文件对应输出流
         */
        private OutputStream outputStream;
        /**
         * 是否自动关闭输入流
         */
        private boolean autoCloseStream = false;
        /**
         * 在内存中编写excel。默认为false，则创建缓存文件并最终写入excel。仅在内存模式下支持Comment和RichTextString
         */
        private boolean inMemory = false;
        /**
         * Excel模板文件路径
         */
        private String template;
        /**
         * Excel模板文件输入流
         */
        private InputStream templateInputStream;
        /**
         * 写入Excel时出现异常是否仍然继续导出
         */
        private boolean writeExcelOnException = false;
        /**
         * 是否自动合并表头
         */
        private boolean automaticMergeHead = true;
        /**
         * 忽略自定义列
         */
        private final List<String> excludeColumnFiledNames = new ArrayList<>();
        /**
         * 忽略自定义列
         */
        private final List<Integer> excludeColumnIndexes = new ArrayList<>();
        /**
         * 只输出自定义列
         */
        private final List<String> includeColumnFiledNames = new ArrayList<>();
        /**
         * 只输出自定义列
         */
        private final List<Integer> includeColumnIndexes = new ArrayList<>();
        /**
         * 是否输出表头
         */
        private boolean needHead = true;
        /**
         * 输出第一行的位置
         */
        private int relativeHeadRowIndex = 0;
        /**
         * 是否使用默认样式
         */
        private boolean useDefaultStyle = true;
        /**
         * Excel类型
         */
        private ExcelTypeEnum excelType = ExcelTypeEnum.XLSX;
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
         * Excel表头
         */
        private final List<HeadConfig> columns = new ArrayList<>();

        public List<List<String>> getHeads() {
            return columns.stream().map(headConfig -> headConfig.names).collect(Collectors.toList());
        }
    }

    @Data
    public static class HeadConfig implements Serializable {
        private final List<String> names = new ArrayList<>();

        public HeadConfig() {
        }

        public HeadConfig(String... names) {
            if (names != null) {
                this.names.addAll(Arrays.asList(names));
            }
        }

    }

    @SuppressWarnings("rawtypes")
    @Slf4j
    private static class ExcelDateReadListener extends AnalysisEventListener<Map<Integer, CellData<?>>> {
        private final ExcelDataReaderConfig config;
        private final ExcelDataReader<Map> excelDataReader;
        /**
         * Map<index, TupleTow<type, head>>
         */
        private final Map<Integer, TupleTow<Class<?>, String>> columns = new HashMap<>();

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

        private Class<?> getCellDataType(CellData<?> cellData) {
            if (cellData.getType() == null) {
                return Void.class;
            }
            switch (cellData.getType()) {
                case NUMBER:
                    return BigDecimal.class;
                case BOOLEAN:
                    return Boolean.class;
                case DIRECT_STRING:
                case STRING:
                case ERROR:
                    return String.class;
                case IMAGE:
                    return Byte[].class;
                default:
                    return Void.class;
            }
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
                if (!columnsConfig.isEmpty()) {
                    clazz = columnsConfig.get(head);
                    if (clazz == null) {
                        continue;
                    }
                }
                columns.put(index, TupleTow.creat(clazz, head));
            }
        }

        @SuppressWarnings("unchecked")
        @Override
        public void invoke(Map<Integer, CellData<?>> data, AnalysisContext context) {
            ExcelData<Map> excelData = getExcelData(context);
            if (excelData.getStartTime() == null) {
                excelData.setStartTime(System.currentTimeMillis());
            }
            int index = context.readRowHolder().getRowIndex() + 1;
            ExcelRow<Map> excelRow = new ExcelRow<>(new HashMap(data.size()), index);
            // 数据签名-防重机制
            Map<Integer, Cell> map = context.readRowHolder().getCellMap();
            StringBuilder sb = new StringBuilder(map.size() * 32);
            for (Map.Entry<Integer, Cell> entry : map.entrySet()) {
                sb.append(entry.getKey()).append("=").append(entry.getValue().toString()).append("|");
            }
            excelRow.setDataSignature(EncodeDecodeUtils.encodeHex(DigestUtils.sha1(sb.toString().getBytes())));
            // 读取数据需要类型转换
            ReadHolder currentReadHolder = context.currentReadHolder();
            ExcelReadHeadProperty excelReadHeadProperty = context.currentReadHolder().excelReadHeadProperty();
            Map<Integer, ExcelContentProperty> contentPropertyMap = excelReadHeadProperty.getContentPropertyMap();
            for (Map.Entry<Integer, CellData<?>> entry : data.entrySet()) {
                Integer idx = entry.getKey();
                CellData<?> cellData = entry.getValue();
                TupleTow<Class<?>, String> tupleTow = columns.get(idx);
                if (tupleTow.getValue1() == null) {
                    tupleTow.setValue1(getCellDataType(cellData));
                }
                Object value;
                if (Objects.equals(Void.class, tupleTow.getValue1())) {
                    value = "";
                } else {
                    ExcelContentProperty excelContentProperty = contentPropertyMap.get(index);
                    value = ConverterUtils.convertToJavaObject(
                            cellData,
                            tupleTow.getValue1(),
                            excelContentProperty,
                            currentReadHolder.converterMap(),
                            currentReadHolder.globalConfiguration(),
                            context.readRowHolder().getRowIndex(), index);
                }
                excelRow.getData().put(tupleTow.getValue2(), value);
            }
            boolean success = true;
            final boolean enableExcelData = config.isEnableExcelData();
            if (enableExcelData) {
                success = excelData.addRow(excelRow);
            }
            if (!success) {
                log.info("Excel数据导入数据重复，filename={} | data={}", config.getFilename(), data);
            }
            // 数据校验
            //final boolean enableValidation = config.isEnableValidation();
            //if (enableValidation && !excelRow.hasError()) {
            //    // TODO 数据校验
            //}
            // 自定义读取行处理逻辑
            final ExcelRowReader<Map> excelRowReader = config.getExcelRowReader();
            if (!excelRow.hasError() && excelRowReader != null) {
                try {
                    excelRowReader.readRow(data, excelRow, context);
                } catch (Throwable e) {
                    excelRow.addErrorInRow(e.getMessage());
                }
            }
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext context) {
            ExcelData<Map> excelData = getExcelData(context);
            if (excelData.getEndTime() == null) {
                excelData.setEndTime(System.currentTimeMillis());
            }
            if (excelData.getEndTime() != null && excelData.getStartTime() != null) {
                log.info("Excel Sheet读取完成，sheet={} | 耗时：{}ms", excelData.getSheetName(), excelData.getEndTime() - excelData.getStartTime());
            }
            ExcelRowReader<Map> excelRowReader = config.getExcelRowReader();
            if (excelRowReader != null) {
                excelRowReader.readEnd(context);
            }
            // if (!enableExcelData) {
            //     excelData.setStartTime(null);
            //     excelData.setEndTime(null);
            // }
        }

        @Override
        public void onException(Exception exception, AnalysisContext context) throws Exception {
            ExcelReaderExceptionHand excelReaderExceptionHand = config.getExcelReaderExceptionHand();
            if (excelReaderExceptionHand != null) {
                excelReaderExceptionHand.exceptionHand(exception, context);
            } else {
                // 默认的异常处理
                throw exception;
            }
        }

        @Override
        public boolean hasNext(AnalysisContext context) {
            // 未配置列 - 提前退出
            if (context.readSheetHolder().getHeadRowNumber() > 0 && columns.isEmpty()) {
                log.warn("未匹配到列配置");
                return false;

            }
            final ExcelData<Map> excelData = getExcelData(context);
            // 是否重复读取
            if (excelData.getEndTime() != null && excelData.getStartTime() != null) {
                log.info("Excel Sheet已经读取完成，当前跳过，sheet={}", excelData.getSheetName());
                return false;
            }
            // 数据是否超出限制 LIMIT_ROWS
            final int limitRows = config.getLimitRows();
            final int rowNum = context.readRowHolder().getRowIndex() + 1;
            final int dataRowNum = rowNum - context.currentReadHolder().excelReadHeadProperty().getHeadRowNumber();
            if (limitRows > 0 && dataRowNum > limitRows) {
                log.info("Excel数据行超出限制：dataRowNum={} | limitRows={}", dataRowNum, limitRows);
                excelData.setInterruptByRowNum(rowNum);
                // 设置已经读取完成
                doAfterAllAnalysed(context);
                return false;
            }
            return true;
        }
    }

    public static class ConverterUtils {
        private ConverterUtils() {
        }

        @SuppressWarnings("rawtypes")
        public static Object convertToJavaObject(
                CellData<?> cellData,
                Class<?> clazz,
                ExcelContentProperty contentProperty,
                Map<String, Converter> converterMap,
                GlobalConfiguration globalConfiguration,
                Integer rowIndex,
                Integer columnIndex) {
            if (clazz == null) {
                clazz = String.class;
            }
            if (Objects.equals(cellData.getType(), CellDataTypeEnum.EMPTY)) {
                if (Objects.equals(String.class, clazz)) {
                    return StringUtils.EMPTY;
                } else {
                    return null;
                }
            }
            Converter<?> converter = null;
            if (contentProperty != null) {
                converter = contentProperty.getConverter();
            }
            if (converter == null) {
                converter = converterMap.get(ConverterKeyBuild.buildKey(clazz, cellData.getType()));
            }
            if (converter == null) {
                throw new ExcelDataConvertException(rowIndex, columnIndex, cellData, contentProperty, "Converter not found, convert " + cellData.getType() + " to " + clazz.getName());
            }
            try {
                return converter.convertToJavaData(cellData, contentProperty, globalConfiguration);
            } catch (Exception e) {
                throw new ExcelDataConvertException(rowIndex, columnIndex, cellData, contentProperty, "Convert data " + cellData + " to " + clazz + " error ", e);
            }
        }
    }

    @Slf4j
    private static class ColumnStyleStrategy extends AbstractCellWriteHandler implements NotRepeatExecutor {
        private final ExcelDataWriterConfig config;

        public ColumnStyleStrategy(ExcelDataWriterConfig config) {
            Assert.notNull(config, "参数config不能为null");
            this.config = config;
        }

        @Override
        public String uniqueValue() {
            return "hinny-core-ColumnStyleStrategy";
        }

//        @Override
//        public void beforeCellCreate(
//                WriteSheetHolder writeSheetHolder,
//                WriteTableHolder writeTableHolder,
//                Row row,
//                Head head,
//                Integer columnIndex,
//                Integer relativeRowIndex,
//                Boolean isHead) {
//            if (isHead) {
//                head.setColumnWidthProperty(new ColumnWidthProperty(60));
//            }
//        }

        int count = 0;


        @Override
        public void afterCellDispose(
                WriteSheetHolder writeSheetHolder,
                WriteTableHolder writeTableHolder,
                List<CellData> cellDataList,
                org.apache.poi.ss.usermodel.Cell cell,
                Head head,
                Integer relativeRowIndex,
                Boolean isHead) {
            log.info("--> {}", (++count));
            writeSheetHolder.getSheet().setColumnWidth(cell.getColumnIndex(), 60 * 256);
        }
    }
}
