package com.vip.boot.autoconfigure.easyexcel.listener;

import com.alibaba.excel.event.AnalysisEventListener;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author echo
 * @version 1.0
 * @date 2023/11/18 20:35
 */
public abstract class ListAnalysisEventListener<T> extends AnalysisEventListener<T> {

    /**
     * 获取 excel 解析的对象列表
     * @return 集合
     */
    public abstract List<T> getList();

    /**
     * 获取异常校验结果
     * @return 集合
     */
    public abstract List<ErrorMessage> getErrors();

    @Data
    @AllArgsConstructor
    public static class ErrorMessage {
        /**
         * 行号
         */
        private Long lineNum;

        /**
         * 错误信息
         */
        private Set<String> errors = new HashSet<>();

        public ErrorMessage(Set<String> errors) {
            this.errors = errors;
        }

        public ErrorMessage(String error) {
            HashSet<String> objects = new HashSet<>();
            objects.add(error);
            this.errors = objects;
        }
    }

}