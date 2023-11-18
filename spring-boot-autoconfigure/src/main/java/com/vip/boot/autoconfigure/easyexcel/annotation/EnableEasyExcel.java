package com.vip.boot.autoconfigure.easyexcel.annotation;

import com.vip.boot.autoconfigure.easyexcel.EasyExcelImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author echo
 * @version 1.0
 * @date 2023/11/18 22:31
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(EasyExcelImportSelector.class)
public @interface EnableEasyExcel {

}