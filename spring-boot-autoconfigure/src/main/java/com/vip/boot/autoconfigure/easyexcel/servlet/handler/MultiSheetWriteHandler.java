package com.vip.boot.autoconfigure.easyexcel.servlet.handler;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.vip.boot.autoconfigure.easyexcel.properties.ExcelConfigProperties;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.ObjectProvider;
import com.vip.boot.autoconfigure.easyexcel.servlet.enhance.WriterBuilderEnhancer;
import com.vip.boot.autoconfigure.easyexcel.head.I18nHeaderCellWriteHandler;
import com.vip.boot.autoconfigure.easyexcel.annotation.ResponseExcel;
import com.vip.boot.autoconfigure.easyexcel.annotation.ResponseExcel.Sheet;
import com.vip.boot.autoconfigure.easyexcel.servlet.handler.SheetWriteHandler.AbstractSheetWriteHandler;

import java.util.List;

/**
 * @author echo
 * @version 1.0
 * @date 2023/11/18 22:06
 */
public class MultiSheetWriteHandler extends AbstractSheetWriteHandler {

    public MultiSheetWriteHandler(ExcelConfigProperties configProperties, ObjectProvider<List<Converter<?>>> converterProvider, WriterBuilderEnhancer enhance, I18nHeaderCellWriteHandler handler) {
        super(configProperties, converterProvider, enhance, handler);
    }

    /**
     * 当且仅当List不为空且List中的元素也是List 才返回true
     * @param responseExcel 返回对象
     * @return 布尔值
     */
    @Override
    public boolean support(ResponseExcel responseExcel) {
        return responseExcel.multi();
    }

    @Override
    @SneakyThrows
    public void write(Object obj, HttpServletResponse response, ResponseExcel responseExcel) {
        List<?> objList = (List<?>) obj;
        ExcelWriter excelWriter = getExcelWriter(response, responseExcel);

        Sheet[] sheets = responseExcel.sheets();
        WriteSheet sheet;
        for (int i = 0; i < sheets.length; i++) {
            List<?> eleList = (List<?>) objList.get(i);
            Class<?> dataClass = eleList.get(0).getClass();
            // 创建sheet
            sheet = this.sheet(sheets[i], dataClass, responseExcel.template(), responseExcel.headGenerator());
            // 写入sheet
            excelWriter.write(eleList, sheet);
        }
        excelWriter.finish();
    }

}
