package com.vip.boot.autoconfigure.easyexcel.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author echo
 * @version 1.0
 * @date 2023/11/18 22:57
 */
@Data
@ConfigurationProperties(prefix = ExcelConfigProperties.PREFIX)
public class ExcelConfigProperties {

    public static final String PREFIX = "easyexcel";

    /**
     * 模板路径
     */
    private String templatePath = "excel";

}