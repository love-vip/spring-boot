package com.vip.boot.autoconfigure.curator;

import com.vip.boot.autoconfigure.support.function.Customizer;
import org.apache.curator.framework.CuratorFrameworkFactory;

/**
 * @author echo
 * @version 1.0
 * @date 2023/4/11 21:39
 */
@FunctionalInterface
public interface CuratorFrameworkBuilderCustomizer extends Customizer<CuratorFrameworkFactory.Builder> {
}