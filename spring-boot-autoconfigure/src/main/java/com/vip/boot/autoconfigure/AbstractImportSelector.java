package com.vip.boot.autoconfigure;

import com.vip.boot.autoconfigure.support.spring.util.AnnotationConfigUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;

/**
 * @author echo
 * @version 1.0
 * @date 2023/4/9 15:26
 */
@Slf4j
public abstract class AbstractImportSelector<A extends Annotation> implements ImportSelector {

    public static final String DEFAULT_LIMIT_MODE_ATTRIBUTE_NAME = "mode";

    @Override
    public final String[] selectImports(AnnotationMetadata importingClassMetadata) {
        Class<?> annType = GenericTypeResolver.resolveTypeArgument(getClass(), AbstractImportSelector.class);
        Assert.state(annType != null, "Unresolvable type argument for AdviceModeImportSelector");

        AnnotationAttributes attributes = AnnotationConfigUtils.attributesFor(importingClassMetadata, annType);
        if (attributes == null) {
            throw new IllegalArgumentException(String.format(
                    "@%s is not present on importing class '%s' as expected",
                    annType.getSimpleName(), importingClassMetadata.getClassName()));
        }

        ActiveModel activeModel = attributes.getEnum(DEFAULT_LIMIT_MODE_ATTRIBUTE_NAME);
        String[] imports = selectImports(activeModel);
        if (imports == null) {
            throw new IllegalArgumentException("Unknown Mode: " + activeModel);
        }
        return imports;
    }

    @Nullable
    protected abstract String[] selectImports(ActiveModel activeModel);
}