package com.vip.boot.autoconfigure.xxl.job;

import com.vip.boot.autoconfigure.xxl.job.properties.XxlJobProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author echo
 * @version 1.0
 * @date 2023/11/18 23:29
 */
@AutoConfiguration
public class XxlJobAutoConfiguration {

    @Slf4j
    @RequiredArgsConstructor
    @EnableConfigurationProperties({XxlJobProperties.class})
    public static class AutoConfiguration{
        private final XxlJobProperties properties;

        @Bean
        @ConditionalOnProperty(prefix = "xxl.job",value = "enable",havingValue = "true")
        public XxlJobSpringExecutor xxlJobExecutor() {
            log.info(">>>>>>>>>>> xxl-job config init.");

            XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
            xxlJobSpringExecutor.setAccessToken(properties.getAccessToken());
            xxlJobSpringExecutor.setAdminAddresses(properties.getAdmin().getAddresses());
            xxlJobSpringExecutor.setAppname(properties.getExecutor().getAppname());
            xxlJobSpringExecutor.setAddress(properties.getExecutor().getAddress());
            xxlJobSpringExecutor.setIp(properties.getExecutor().getIp());
            xxlJobSpringExecutor.setPort(properties.getExecutor().getPort());
            xxlJobSpringExecutor.setLogPath(properties.getExecutor().getLogpath());
            xxlJobSpringExecutor.setLogRetentionDays(properties.getExecutor().getLogretentiondays());

            return xxlJobSpringExecutor;
        }
    }
}
