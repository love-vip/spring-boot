package com.vip.boot.autoconfigure.easyexcel.annotation;

import com.vip.boot.autoconfigure.easyexcel.listener.DefaultAnalysisEventListener;
import com.vip.boot.autoconfigure.easyexcel.listener.ListAnalysisEventListener;

import java.lang.annotation.*;

/**
 * @author echo
 * @version 1.0
 * @date 2023/11/18 20:31
 */
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RequestExcel {

    /**
     * 前端上传字段名称 file
     */
    String fileName() default "file";

    /**
     * 读取的监听器类
     * @return readListener
     */
    Class<? extends ListAnalysisEventListener<?>> readListener() default DefaultAnalysisEventListener.class;

    /**
     * 是否跳过空行
     * @return 默认跳过
     */
    boolean ignoreEmptyRow() default false;

}
