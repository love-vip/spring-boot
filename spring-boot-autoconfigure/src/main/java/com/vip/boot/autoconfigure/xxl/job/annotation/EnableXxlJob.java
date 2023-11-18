package com.vip.boot.autoconfigure.xxl.job.annotation;

import com.vip.boot.autoconfigure.xxl.job.XxlJobImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author echo
 * @version 1.0
 * @date 2023/11/18 23:27
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({XxlJobImportSelector.class})
public @interface EnableXxlJob {

}