package com.vip.boot.autoconfigure.limit.executor;

import cn.hutool.core.util.StrUtil;
import com.vip.boot.autoconfigure.limit.exception.LimitException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.time.Instant;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author echo
 * @version 1.0
 * @date 2023/4/8 21:58
 */
@Slf4j
@RequiredArgsConstructor
public class RedisLimitExecutor implements LimitExecutor {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final DefaultRedisScript<Boolean> SCRIPT;

    static {
        SCRIPT = new DefaultRedisScript<>();
        SCRIPT.setLocation(new ClassPathResource("token_bucket_limiter.lua"));
        SCRIPT.setResultType(Boolean.class);
    }

    @Override
    public boolean tryAccess(String compositeKey, int rate, int rateInterval, TimeUnit rateIntervalUnit) {
        if (StrUtil.isEmpty(compositeKey)) {
            throw new LimitException("Composite key is null or empty");
        }
        /*
         * ARGV[1]：令牌桶最大长度
         * ARGV[2]：当前时间戳
         * ARGV[3]：重置桶内令牌的时间间隔(毫秒)
         * ARGV[4]：单位时间应该放入的令牌数
         * ARGV[5]：本次申请的令牌数量
         */
        return Boolean.TRUE.equals(redisTemplate.execute(SCRIPT, Collections.singletonList(compositeKey),
                rate,
                Instant.now().toEpochMilli(),
                rateIntervalUnit.toMillis(rateInterval),
                rate,
                1));
    }

}