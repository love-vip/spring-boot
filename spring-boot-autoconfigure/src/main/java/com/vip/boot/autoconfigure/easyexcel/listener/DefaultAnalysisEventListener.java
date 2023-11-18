package com.vip.boot.autoconfigure.easyexcel.listener;

import com.alibaba.excel.context.AnalysisContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author echo
 * @version 1.0
 * <P>默认的 AnalysisEventListener</P>
 * @date 2023/11/18 20:33
 */
@Slf4j
public class DefaultAnalysisEventListener extends ListAnalysisEventListener<Object> {

    private final List<Object> list = new ArrayList<>();

    private final List<ErrorMessage> errorMessageList = new ArrayList<>();

    private Long lineNum = 1L;

    @Override
    public void invoke(Object object, AnalysisContext analysisContext) {
        lineNum++;
        @Cleanup
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Set<ConstraintViolation<Object>> violations = factory.getValidator().validate(object);
        if (violations.isEmpty()) {
            list.add(object);
        }
        if (! violations.isEmpty()) {
            Set<String> messageSet = violations.stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());
            errorMessageList.add(new ErrorMessage(lineNum, messageSet));
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        log.info("Excel read analysed sheet: [{}]", analysisContext.readSheetHolder().getSheetName());
    }

    @Override
    public List<Object> getList() {
        return list;
    }

    @Override
    public List<ErrorMessage> getErrors() {
        return errorMessageList;
    }

}
