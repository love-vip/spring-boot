package com.vip.boot.autoconfigure;

import com.vip.boot.autoconfigure.support.spring.util.AnnotationConfigUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.util.LinkedHashSet;
import java.util.Objects;

/**
 * @author echo
 * @version 1.0
 * @date 2023/4/11 18:24
 */
public abstract class AbstractImportRegistrar<A extends Annotation> implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        Class<?> annType = GenericTypeResolver.resolveTypeArgument(getClass(), AbstractImportRegistrar.class);
        Assert.state(annType != null, "Unresolvable type argument for AdviceModeImportSelector");

        AnnotationAttributes attributes = AnnotationConfigUtils.attributesFor(importingClassMetadata, annType);
        Assert.notNull(attributes, () -> String.format("@%s is not present on importing class '%s' as expected",annType.getSimpleName(), importingClassMetadata.getClassName()));

        //获取包扫描
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);

        LinkedHashSet<BeanDefinition> candidateComponents = new LinkedHashSet<>();

        for (String basePackages : attributes.getStringArray("basePackages")) {
            candidateComponents.addAll(provider.findCandidateComponents(basePackages));
        }

        //注册Bean
        for (BeanDefinition candidate : candidateComponents) {
            registry.registerBeanDefinition(Objects.requireNonNull(candidate.getBeanClassName()), candidate);
        }
    }
}
