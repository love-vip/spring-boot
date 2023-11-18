package com.vip.boot.autoconfigure.easyexcel.servlet.resolver;

import com.alibaba.excel.EasyExcel;
import com.vip.boot.autoconfigure.easyexcel.annotation.RequestExcel;
import com.vip.boot.autoconfigure.easyexcel.converter.LocalDateStringConverter;
import com.vip.boot.autoconfigure.easyexcel.converter.LocalDateTimeStringConverter;
import com.vip.boot.autoconfigure.easyexcel.listener.ListAnalysisEventListener;
import jakarta.servlet.http.HttpServletRequest;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.io.InputStream;
import java.util.List;
import java.util.Objects;

/**
 * @author echo
 * @version 1.0
 * @date 2023/11/18 22:16
 */
@Slf4j
public class RequestExcelArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getMethodAnnotation(RequestExcel.class) != null;
    }

    @Override
    @SneakyThrows
    public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer modelAndViewContainer, @NonNull NativeWebRequest webRequest, @Nullable WebDataBinderFactory webDataBinderFactory) {
        Class<?> parameterType = parameter.getParameterType();
        if (!parameterType.isAssignableFrom(List.class)) {
            throw new IllegalArgumentException("Excel upload request resolver error, @RequestExcel parameter is not List " + parameterType);
        }

        // 处理自定义 readListener
        RequestExcel requestExcel = Objects.requireNonNull(parameter.getMethodAnnotation(RequestExcel.class));
        Class<? extends ListAnalysisEventListener<?>> readListenerClass = requestExcel.readListener();
        ListAnalysisEventListener<?> readListener = BeanUtils.instantiateClass(readListenerClass);

        // 获取请求文件流
        HttpServletRequest request = Objects.requireNonNull(webRequest.getNativeRequest(HttpServletRequest.class));
        InputStream inputStream;
        if (request instanceof MultipartRequest) {
            MultipartFile file = Objects.requireNonNull(((MultipartRequest) request).getFile(requestExcel.fileName()));
            inputStream = file.getInputStream();
        }
        else {
            inputStream = request.getInputStream();
        }

        // 获取目标类型
        Class<?> excelModelClass = ResolvableType.forMethodParameter(parameter).getGeneric(0).resolve();

        // 这里需要指定读用哪个 class 去读，然后读取第一个 sheet 文件流会自动关闭
        EasyExcel.read(inputStream, excelModelClass, readListener)
                .registerConverter(LocalDateStringConverter.INSTANCE)
                .registerConverter(LocalDateTimeStringConverter.INSTANCE)
                .ignoreEmptyRow(requestExcel.ignoreEmptyRow())
                .sheet().doRead();

        // 校验失败的数据处理 交给 BindResult
        WebDataBinder dataBinder = webDataBinderFactory.createBinder(webRequest, readListener.getErrors(), "excel");
        ModelMap model = modelAndViewContainer.getModel();
        model.put(BindingResult.MODEL_KEY_PREFIX + "excel", dataBinder.getBindingResult());

        return readListener.getList();
    }

}