package com.vip.boot.autoconfigure.easyexcel.annotation;

import com.vip.boot.autoconfigure.easyexcel.EasyExcelImportRegistrar;
import com.vip.boot.autoconfigure.easyexcel.EasyExcelImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author echo
 * @version 1.0
 * @date 2023/11/18 22:31
 */
@Documented
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({EasyExcelImportRegistrar.class, EasyExcelImportSelector.class})
public @interface EnableEasyExcel {

}