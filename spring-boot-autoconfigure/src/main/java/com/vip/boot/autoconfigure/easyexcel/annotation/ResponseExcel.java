package com.vip.boot.autoconfigure.easyexcel.annotation;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.handler.WriteHandler;
import com.vip.boot.autoconfigure.easyexcel.head.HeadGenerator;

import java.lang.annotation.*;

/**
 * @author echo
 * @version 1.0
 * @date 2023/11/18 20:31
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseExcel {

    /**
     * 文件名称
     * @return string
     */
    String name() default "";

    /**
     * 资源的key
     * @return String
     */
    String key() default "";

    /**
     * 文件类型 （xlsx xls）
     * @return string
     */
    ExcelTypeEnum suffix() default ExcelTypeEnum.XLSX;

    /**
     * 文件密码
     * @return password
     */
    String password() default "";

    /**
     * sheet 名称，支持多个
     * @return String[]
     */
    Sheet[] sheets() default @Sheet(sheetName = "sheet1");

    /**
     * 内存操作
     * @return
     */
    boolean inMemory() default false;

    /**
     * excel 模板
     * @return String
     */
    String template() default "";

    /**
     * + 包含字段
     * @return String[]
     */
    String[] include() default {};

    /**
     * 排除字段
     * @return String[]
     */
    String[] exclude() default {};

    /**
     * 拦截器，自定义样式等处理器
     * @return WriteHandler[]
     */
    Class<? extends WriteHandler>[] writeHandler() default {};

    /**
     * 转换器
     * @return Converter[]
     */
    Class<? extends Converter>[] converter() default {};

    /**
     * 自定义Excel头生成器
     * @return HeadGenerator
     */
    Class<? extends HeadGenerator> headGenerator() default HeadGenerator.class;

    /**
     * excel 头信息国际化
     * @return boolean
     */
    boolean i18nHeader() default false;

    /**
     * excel 多sheet
     * @return boolean
     */
    boolean multi() default false;

    @Documented
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface Sheet {

        int sheetNo() default -1;

        /**
         * sheet name
         */
        String sheetName();

        /**
         * 包含字段
         */
        String[] includes() default {};

        /**
         * 排除字段
         */
        String[] excludes() default {};

        /**
         * 头生成器
         */
        Class<? extends HeadGenerator> headGenerateClass() default HeadGenerator.class;

    }
}
