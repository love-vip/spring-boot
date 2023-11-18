package com.vip.boot.autoconfigure.easyexcel.head;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * @author echo
 * @version 1.0
 * <p>Excel头生成器，用于自定义生成头部信息</p>
 * @date 2023/11/18 21:01
 */
public interface HeadGenerator {

    /**
     * <p>
     * 自定义头部信息
     * </p>
     * 实现类根据数据的class信息，定制Excel头<br/>
     * 具体方法使用参考：<a href="https://www.yuque.com/easyexcel/doc/write#b4b9de00">
     * @param clazz 当前sheet的数据类型
     * @return List<List < String>> Head头信息
     */
    HeadMeta head(Class<?> clazz);

    @Data
    class HeadMeta {
        /**
         * <p>
         * 自定义头部信息
         * </p>
         * 实现类根据数据的class信息，定制Excel头<br/>
         * 具体方法使用参考：<a href="https://www.yuque.com/easyexcel/doc/write#b4b9de00">
         */
        private List<List<String>> head;

        /**
         * 忽略头对应字段名称
         */
        private Set<String> ignoreHeadFields;
    }

}