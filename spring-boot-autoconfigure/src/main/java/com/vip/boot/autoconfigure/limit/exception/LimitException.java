package com.vip.boot.autoconfigure.limit.exception;

import java.io.Serial;

/**
 * @author echo
 * @version 1.0
 * @date 2023/4/8 21:55
 */
public class LimitException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 5523106933655728813L;

    public LimitException() {
        super();
    }

    public LimitException(String message) {
        super(message);
    }

    public LimitException(String message, Throwable cause) {
        super(message, cause);
    }

    public LimitException(Throwable cause) {
        super(cause);
    }
}