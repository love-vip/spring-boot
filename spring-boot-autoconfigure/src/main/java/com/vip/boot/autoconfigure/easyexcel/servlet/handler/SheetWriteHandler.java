package com.vip.boot.autoconfigure.easyexcel.servlet.handler;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.excel.write.builder.ExcelWriterSheetBuilder;
import com.alibaba.excel.write.handler.WriteHandler;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.vip.boot.autoconfigure.easyexcel.EasyExcelInterceptor;
import com.vip.boot.autoconfigure.easyexcel.annotation.ResponseExcel;
import com.vip.boot.autoconfigure.easyexcel.converter.LocalDateStringConverter;
import com.vip.boot.autoconfigure.easyexcel.converter.LocalDateTimeStringConverter;
import com.vip.boot.autoconfigure.easyexcel.properties.ExcelConfigProperties;
import com.vip.boot.autoconfigure.easyexcel.servlet.enhance.WriterBuilderEnhancer;
import com.vip.boot.autoconfigure.easyexcel.exception.ExcelException;
import com.vip.boot.autoconfigure.easyexcel.head.HeadGenerator;
import com.vip.boot.autoconfigure.easyexcel.head.I18nHeaderCellWriteHandler;
import com.vip.boot.autoconfigure.support.ThreadLocalContext;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Modifier;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

/**
 * @author echo
 * @version 1.0
 * @date 2023/11/18 21:48
 */
public interface SheetWriteHandler {

    /**
     * 是否支持
     * @param responseExcel
     * @return
     */
    boolean support(ResponseExcel responseExcel);

    /**
     * 校验
     * @param responseExcel 注解
     */
    void check(ResponseExcel responseExcel);

    /**
     * 返回的对象
     * @param o obj
     * @param response 输出对象
     * @param responseExcel 注解
     */
    void export(Object o, HttpServletResponse response, ResponseExcel responseExcel);

    /**
     * 写成对象
     * @param o obj
     * @param response 输出对象
     * @param responseExcel 注解
     */
    void write(Object o, HttpServletResponse response, ResponseExcel responseExcel);

    @RequiredArgsConstructor
    abstract class AbstractSheetWriteHandler implements SheetWriteHandler, ApplicationContextAware {

        private final ExcelConfigProperties configProperties;

        private final ObjectProvider<List<Converter<?>>> converterProvider;

        private final WriterBuilderEnhancer excelWriterBuilderEnhance;

        private ApplicationContext applicationContext;

        private final I18nHeaderCellWriteHandler i18nHeaderCellWriteHandler;

        @Override
        public void check(ResponseExcel responseExcel) {
            if (responseExcel.sheets().length == 0) {
                throw new ExcelException("@ResponseExcel sheet 配置不合法");
            }
        }

        @Override
        @SneakyThrows
        public void export(Object o, HttpServletResponse response, ResponseExcel responseExcel) {
            check(responseExcel);
            String name = ThreadLocalContext.get(EasyExcelInterceptor.EXCEL_NAME_KEY);
            String fileName = String.format("%s%s", URLEncoder.encode(name, StandardCharsets.UTF_8), responseExcel.suffix().getValue());
            // 根据实际的文件类型找到对应的 contentType
            String contentType = MediaTypeFactory.getMediaType(fileName).map(MediaType::toString).orElse("application/vnd.ms-excel");
            response.setContentType(contentType);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);
            write(o, response, responseExcel);
            ThreadLocalContext.remove(EasyExcelInterceptor.EXCEL_NAME_KEY);
        }

        /**
         * 通用的获取ExcelWriter方法
         * @param response HttpServletResponse
         * @param responseExcel ResponseExcel注解
         * @return ExcelWriter
         */
        @SneakyThrows
        public ExcelWriter getExcelWriter(HttpServletResponse response, ResponseExcel responseExcel) {
            ExcelWriterBuilder writerBuilder = EasyExcel.write(response.getOutputStream())
                    .registerConverter(LocalDateStringConverter.INSTANCE)
                    .registerConverter(LocalDateTimeStringConverter.INSTANCE).autoCloseStream(true)
                    .excelType(responseExcel.suffix()).inMemory(responseExcel.inMemory());

            if (StringUtils.hasText(responseExcel.password())) {
                writerBuilder.password(responseExcel.password());
            }

            if (responseExcel.include().length != 0) {
                writerBuilder.includeColumnFieldNames(Arrays.asList(responseExcel.include()));
            }

            if (responseExcel.exclude().length != 0) {
                writerBuilder.includeColumnFieldNames(Arrays.asList(responseExcel.exclude()));
            }

            for (Class<? extends WriteHandler> clazz : responseExcel.writeHandler()) {
                writerBuilder.registerWriteHandler(BeanUtils.instantiateClass(clazz));
            }

            // 开启国际化头信息处理
            if (responseExcel.i18nHeader() && i18nHeaderCellWriteHandler != null) {
                writerBuilder.registerWriteHandler(i18nHeaderCellWriteHandler);
            }

            // 自定义注入的转换器
            registerCustomConverter(writerBuilder);

            for (Class<? extends Converter<?>> clazz : responseExcel.converter()) {
                writerBuilder.registerConverter(BeanUtils.instantiateClass(clazz));
            }

            String templatePath = configProperties.getTemplatePath();
            if (StringUtils.hasText(responseExcel.template())) {
                ClassPathResource classPathResource = new ClassPathResource(templatePath + File.separator + responseExcel.template());
                InputStream inputStream = classPathResource.getInputStream();
                writerBuilder.withTemplate(inputStream);
            }

            writerBuilder = excelWriterBuilderEnhance.enhanceExcel(writerBuilder, response, responseExcel, templatePath);

            return writerBuilder.build();
        }

        /**
         * 自定义注入转换器 如果有需要，子类自己重写
         * @param builder ExcelWriterBuilder
         */
        public void registerCustomConverter(ExcelWriterBuilder builder) {
            converterProvider.ifAvailable(converters -> converters.forEach(builder::registerConverter));
        }

        /**
         * 获取 WriteSheet 对象
         * @param sheet sheet annotation info
         * @param dataClass 数据类型
         * @param template 模板
         * @param bookHeadEnhancerClass 自定义头处理器
         * @return WriteSheet
         */
        public WriteSheet sheet(ResponseExcel.Sheet sheet, Class<?> dataClass, String template, Class<? extends HeadGenerator> bookHeadEnhancerClass) {

            // Sheet 编号和名称
            Integer sheetNo = sheet.sheetNo() >= 0 ? sheet.sheetNo() : null;
            String sheetName = sheet.sheetName();

            // 是否模板写入
            ExcelWriterSheetBuilder writerSheetBuilder = StringUtils.hasText(template) ? EasyExcel.writerSheet(sheetNo)
                    : EasyExcel.writerSheet(sheetNo, sheetName);

            // 头信息增强 1. 优先使用 sheet 指定的头信息增强 2. 其次使用 @ResponseExcel 中定义的全局头信息增强
            Class<? extends HeadGenerator> headGenerateClass = null;
            if (isNotInterface(sheet.headGenerateClass())) {
                headGenerateClass = sheet.headGenerateClass();
            }
            else if (isNotInterface(bookHeadEnhancerClass)) {
                headGenerateClass = bookHeadEnhancerClass;
            }
            // 定义头信息增强则使用其生成头信息，否则使用 dataClass 来自动获取
            if (headGenerateClass != null) {
                fillCustomHeadInfo(dataClass, bookHeadEnhancerClass, writerSheetBuilder);
            }
            else if (dataClass != null) {
                writerSheetBuilder.head(dataClass);
                if (sheet.excludes().length > 0) {
                    writerSheetBuilder.excludeColumnFieldNames(Arrays.asList(sheet.excludes()));
                }
                if (sheet.includes().length > 0) {
                    writerSheetBuilder.includeColumnFieldNames(Arrays.asList(sheet.includes()));
                }
            }

            // sheetBuilder 增强
            writerSheetBuilder = excelWriterBuilderEnhance.enhanceSheet(writerSheetBuilder, sheetNo, sheetName, dataClass, template, headGenerateClass);

            return writerSheetBuilder.build();
        }

        private void fillCustomHeadInfo(Class<?> dataClass, Class<? extends HeadGenerator> headEnhancerClass, ExcelWriterSheetBuilder writerSheetBuilder) {
            HeadGenerator headGenerator = this.applicationContext.getBean(headEnhancerClass);
            Assert.notNull(headGenerator, "The header generated bean does not exist.");
            HeadGenerator.HeadMeta head = headGenerator.head(dataClass);
            writerSheetBuilder.head(head.getHead());
            writerSheetBuilder.excludeColumnFieldNames(head.getIgnoreHeadFields());
        }

        /**
         * 是否为Null Head Generator
         * @param headGeneratorClass
         * @return true 已指定 false 未指定(默认值)
         */
        private boolean isNotInterface(Class<? extends HeadGenerator> headGeneratorClass) {
            return !Modifier.isInterface(headGeneratorClass.getModifiers());
        }

        @Override
        public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
            this.applicationContext = applicationContext;
        }

    }


}