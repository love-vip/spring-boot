package com.vip.boot.autoconfigure.easyexcel.servlet.resolver;

import com.vip.boot.autoconfigure.easyexcel.annotation.ResponseExcel;
import com.vip.boot.autoconfigure.easyexcel.servlet.handler.SheetWriteHandler;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

/**
 * @author echo
 * @version 1.0
 * @date 2023/11/18 22:17
 */
@Slf4j
@RequiredArgsConstructor
public class ResponseExcelReturnValueHandler implements HandlerMethodReturnValueHandler {

    private final List<SheetWriteHandler> sheetWriteHandlerList;

    /**
     * 只处理@ResponseExcel 声明的方法
     * @param parameter 方法签名
     * @return 是否处理
     */
    @Override
    public boolean supportsReturnType(MethodParameter parameter) {
        return parameter.getMethodAnnotation(ResponseExcel.class) != null;
    }

    /**
     * 处理逻辑
     * @param returnValue 返回参数
     * @param returnType 方法签名
     * @param mavContainer 上下文容器
     * @param nativeWebRequest 上下文
     */
    @Override
    public void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest nativeWebRequest) {

        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        Assert.state(response != null, "No HttpServletResponse");

        ResponseExcel responseExcel = returnType.getMethodAnnotation(ResponseExcel.class);
        Assert.state(responseExcel != null, "No @ResponseExcel");

        mavContainer.setRequestHandled(true);

        sheetWriteHandlerList.stream()
                .filter(handler -> handler.support(responseExcel))
                .findFirst()
                .ifPresent(handler -> handler.export(returnValue, response, responseExcel));
    }

}