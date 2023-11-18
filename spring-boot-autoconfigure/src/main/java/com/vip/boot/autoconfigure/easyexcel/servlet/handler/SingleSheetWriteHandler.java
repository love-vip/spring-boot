package com.vip.boot.autoconfigure.easyexcel.servlet.handler;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.vip.boot.autoconfigure.easyexcel.annotation.ResponseExcel;
import com.vip.boot.autoconfigure.easyexcel.properties.ExcelConfigProperties;
import com.vip.boot.autoconfigure.easyexcel.servlet.enhance.WriterBuilderEnhancer;
import com.vip.boot.autoconfigure.easyexcel.head.I18nHeaderCellWriteHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.ObjectProvider;
import com.vip.boot.autoconfigure.easyexcel.servlet.handler.SheetWriteHandler.AbstractSheetWriteHandler;

import java.util.List;

/**
 * @author echo
 * @version 1.0
 * @date 2023/11/18 22:06
 */
public class SingleSheetWriteHandler extends AbstractSheetWriteHandler {

    public SingleSheetWriteHandler(ExcelConfigProperties configProperties, ObjectProvider<List<Converter<?>>> converterProvider, WriterBuilderEnhancer enhance, I18nHeaderCellWriteHandler handler) {
        super(configProperties, converterProvider, enhance, handler);
    }

    /**
     * obj 是List 且list不为空同时list中的元素不是是List 才返回true
     * @param responseExcel 返回对象
     * @return 布尔值
     */
    @Override
    public boolean support(ResponseExcel responseExcel) {
        return ! responseExcel.multi();
    }

    @Override
    @SneakyThrows
    public void write(Object obj, HttpServletResponse response, ResponseExcel responseExcel) {
        List<?> list = (List<?>) obj;
        ExcelWriter excelWriter = getExcelWriter(response, responseExcel);
        // 有模板则不指定sheet名
        Class<?> dataClass = list.get(0).getClass();
        WriteSheet sheet = this.sheet(responseExcel.sheets()[0], dataClass, responseExcel.template(), responseExcel.headGenerator());
        excelWriter.write(list, sheet);
        excelWriter.finish();
    }

}