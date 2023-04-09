package com.vip.boot.autoconfigure.support.util;

import lombok.experimental.UtilityClass;

/**
 * @author echo
 * @version 1.0
 * @date 2023/4/9 10:26
 */
@UtilityClass
public class KeyUtil {

    public static String getCompositeKey(String prefix, String name, String key) {
        return prefix + ":" + name + ":" + key;
    }

    public static String getCompositeWildcardKey(String prefix, String name) {
        return prefix + ":" + name + "*";
    }

    public static String getCompositeWildcardKey(String key) {
        return key + "*";
    }
}
