package com.vip.boot.autoconfigure.limit.executor;

import cn.hutool.core.util.StrUtil;
import com.vip.boot.autoconfigure.limit.exception.LimitException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @author echo
 * @version 1.0
 * @date 2023/4/8 22:21
 */
@Slf4j
@RequiredArgsConstructor
public class RedissonLimitExecutor implements LimitExecutor {

    private final RedissonClient redissonClient;

    @Override
    public boolean tryAccess(String compositeKey, int rate, int rateInterval, TimeUnit rateIntervalUnit) {
        if (StrUtil.isEmpty(compositeKey)) {
            throw new LimitException("Composite key is null or empty");
        }

        RRateLimiter rateLimiter = redissonClient.getRateLimiter(compositeKey);

        switch (rateIntervalUnit) {
            case MILLISECONDS ->
                /* 设置速率，rateInterval 毫秒中产生 rate 个令牌 */
                    rateLimiter.trySetRate(RateType.OVERALL, rate, rateInterval, RateIntervalUnit.MILLISECONDS);
            case SECONDS ->
                /* 设置速率，rateInterval 秒中产生 rate 个令牌 */
                    rateLimiter.trySetRate(RateType.OVERALL, rate, rateInterval, RateIntervalUnit.SECONDS);
            case MINUTES ->
                /* 设置速率，rateInterval 分钟中产生 rate 个令牌 */
                    rateLimiter.trySetRate(RateType.OVERALL, rate, rateInterval, RateIntervalUnit.MINUTES);
            case HOURS ->
                /* 设置速率，rateInterval 小时中产生 rate 个令牌 */
                    rateLimiter.trySetRate(RateType.OVERALL, rate, rateInterval, RateIntervalUnit.HOURS);
            default -> log.error("un support TimeUnit {}", rateIntervalUnit);
        }

        /* 试图获取1个令牌，获取到返回true */
        return rateLimiter.tryAcquire(1);
    }

}