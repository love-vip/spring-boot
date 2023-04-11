package com.vip.boot.autoconfigure.curator;

import com.vip.boot.autoconfigure.curator.template.CuratorTemplate;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author echo
 * @version 1.0
 * @date 2023/4/11 21:45
 */
@AutoConfiguration
@ConditionalOnClass(CuratorFramework.class)
@EnableConfigurationProperties(CuratorProperties.class)
public class CuratorAutoConfiguration {

    /**
     * Curator framework
     *
     * @param curatorProperties                  the curator properties
     * @param curatorFrameworkBuilderCustomizers the curator framework builder customizers
     * @return the curator framework
     */
    @ConditionalOnMissingBean
    @Bean(initMethod = "start", destroyMethod = "close")
    public CuratorFramework curatorFramework(CuratorProperties curatorProperties,
                                             ObjectProvider<CuratorFrameworkBuilderCustomizer> curatorFrameworkBuilderCustomizers) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(curatorProperties.getBaseSleepTime(), curatorProperties.getMaxRetries());
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder()
                .connectString(curatorProperties.getServers())
                .connectionTimeoutMs(curatorProperties.getConnectionTimeout())
                .sessionTimeoutMs(curatorProperties.getSessionTimeout())
                .retryPolicy(retryPolicy);
        curatorFrameworkBuilderCustomizers.orderedStream().forEach(customizer -> customizer.customize(builder));
        return builder.build();
    }

    /**
     * Curator template curator template.
     *
     * @param curatorFramework the curator framework
     * @return the curator template
     */
    @Bean
    @ConditionalOnMissingBean
    public CuratorTemplate curatorTemplate(CuratorFramework curatorFramework) {
        return new CuratorTemplate(curatorFramework);
    }
}
