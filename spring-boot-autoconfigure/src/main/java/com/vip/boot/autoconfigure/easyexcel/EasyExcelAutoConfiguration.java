package com.vip.boot.autoconfigure.easyexcel;

import com.alibaba.excel.converters.Converter;
import com.vip.boot.autoconfigure.easyexcel.head.I18nHeaderCellWriteHandler;
import com.vip.boot.autoconfigure.easyexcel.properties.ExcelConfigProperties;
import com.vip.boot.autoconfigure.easyexcel.servlet.enhance.WriterBuilderEnhancer;
import com.vip.boot.autoconfigure.easyexcel.servlet.handler.SingleSheetWriteHandler;
import com.vip.boot.autoconfigure.easyexcel.servlet.resolver.RequestExcelArgumentResolver;
import com.vip.boot.autoconfigure.easyexcel.servlet.resolver.ResponseExcelReturnValueHandler;
import com.vip.boot.autoconfigure.easyexcel.servlet.handler.SheetWriteHandler;
import com.vip.boot.autoconfigure.easyexcel.servlet.handler.MultiSheetWriteHandler;
import com.vip.boot.autoconfigure.limit.executor.LimitExecutor;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author echo
 * @version 1.0
 * @date 2023/11/18 22:26
 */
@AutoConfiguration
public class EasyExcelAutoConfiguration {

    @Bean
    @ConditionalOnBean(LimitExecutor.class)
    public EasyExcelInterceptor excelInterceptor() {
        return new EasyExcelInterceptor();
    }

    @RequiredArgsConstructor
    @EnableConfigurationProperties(ExcelConfigProperties.class)
    @ConditionalOnProperty(prefix = "easyexcel", value = "enabled", havingValue = "true")
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public static class ResponseExcelAutoConfiguration {

        private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

        private final ResponseExcelReturnValueHandler responseExcelReturnValueHandler;

        /**
         * 追加 Excel 请求处理器 到 SpringMVC 中
         */
        @PostConstruct
        public void setRequestExcelArgumentResolver() {
            List<HandlerMethodArgumentResolver> argumentResolvers = Objects.requireNonNull(requestMappingHandlerAdapter.getArgumentResolvers());

            List<HandlerMethodArgumentResolver> resolverList = new ArrayList<>();
            resolverList.add(new RequestExcelArgumentResolver());
            resolverList.addAll(argumentResolvers);
            requestMappingHandlerAdapter.setArgumentResolvers(resolverList);
        }

        /**
         * 追加 Excel返回值处理器 到 SpringMVC 中
         */
        @PostConstruct
        public void setReturnValueHandlers() {
            List<HandlerMethodReturnValueHandler> returnValueHandlers = Objects.requireNonNull(requestMappingHandlerAdapter.getReturnValueHandlers());

            List<HandlerMethodReturnValueHandler> newHandlers = new ArrayList<>();
            newHandlers.add(responseExcelReturnValueHandler);
            newHandlers.addAll(returnValueHandlers);
            requestMappingHandlerAdapter.setReturnValueHandlers(newHandlers);
        }

    }


    @RequiredArgsConstructor
    @EnableConfigurationProperties(ExcelConfigProperties.class)
    @AutoConfigureBefore(ResponseExcelAutoConfiguration.class)
    @ConditionalOnProperty(prefix = "easyexcel", value = "enabled", havingValue = "true")
    @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
    public static class ExcelHandlerConfiguration {

        private final ExcelConfigProperties configProperties;

        private final ObjectProvider<List<Converter<?>>> converterProvider;

        /**
         * ExcelBuild增强
         * @return DefaultWriterBuilderEnhancer 默认什么也不做的增强器
         */
        @Bean
        @ConditionalOnMissingBean
        public WriterBuilderEnhancer writerBuilderEnhancer() {
            return new WriterBuilderEnhancer.DefaultWriterBuilderEnhancer();
        }

        /**
         * 单sheet 写入处理器
         */
        @Bean
        @ConditionalOnMissingBean
        public SingleSheetWriteHandler singleSheetWriteHandler(I18nHeaderCellWriteHandler i18nHeaderCellWriteHandler) {
            return new SingleSheetWriteHandler(configProperties, converterProvider, writerBuilderEnhancer(), i18nHeaderCellWriteHandler);
        }

        /**
         * 多sheet 写入处理器
         */
        @Bean
        @ConditionalOnMissingBean
        public MultiSheetWriteHandler manySheetWriteHandler(I18nHeaderCellWriteHandler i18nHeaderCellWriteHandler) {
            return new MultiSheetWriteHandler(configProperties, converterProvider, writerBuilderEnhancer(), i18nHeaderCellWriteHandler);
        }

        /**
         * 返回Excel文件的 response 处理器
         * @param sheetWriteHandlerList 页签写入处理器集合
         * @return ResponseExcelReturnValueHandler
         */
        @Bean
        @ConditionalOnMissingBean
        public ResponseExcelReturnValueHandler responseExcelReturnValueHandler(List<SheetWriteHandler> sheetWriteHandlerList) {
            return new ResponseExcelReturnValueHandler(sheetWriteHandlerList);
        }

        /**
         * excel 头的国际化处理器
         * @param messageSource 国际化源
         */
        @Bean
        @ConditionalOnMissingBean
        @ConditionalOnBean(MessageSource.class)
        public I18nHeaderCellWriteHandler i18nHeaderCellWriteHandler(MessageSource messageSource) {
            return new I18nHeaderCellWriteHandler(messageSource);
        }

    }

}
