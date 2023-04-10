package com.vip.boot.autoconfigure.limit.executor;

import java.util.concurrent.TimeUnit;

/**
 * @author echo
 * @version 1.0
 * @date 2023/4/8 21:43
 */
public interface LimitExecutor {

    /**
     * 在给定的时间段里最多的访问限制次数(超出次数返回false)；等下个时间段开始，才允许再次被访问(返回true)，周而复始
     * @param key 资源Key
     * @param rateInterval 给定的时间段(单位秒)
     * @param rate 最多的访问限制次数
     * @return boolean
     */
    boolean tryAccess(String key, int rate, int rateInterval, TimeUnit rateIntervalUnit);
}
