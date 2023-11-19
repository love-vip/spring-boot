package com.vip.boot.autoconfigure.cache.exception;

import java.io.Serial;

/**
 * @author echo
 * @version 1.0
 * @date 2023/11/19 22:08
 */
public class CacheException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 5523106933655728813L;

    public CacheException() {
        super();
    }

    public CacheException(String message) {
        super(message);
    }

    public CacheException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheException(Throwable cause) {
        super(cause);
    }
}