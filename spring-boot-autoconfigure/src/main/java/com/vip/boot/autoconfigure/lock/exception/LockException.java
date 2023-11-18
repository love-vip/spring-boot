package com.vip.boot.autoconfigure.lock.exception;

import java.io.Serial;

/**
 * @author echo
 * @version 1.0
 * @date 2023/4/9 22:19
 */
public class LockException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -5563106933655728813L;

    public LockException() {
        super();
    }

    public LockException(String message) {
        super(message);
    }

    public LockException(String message, Throwable cause) {
        super(message, cause);
    }

    public LockException(Throwable cause) {
        super(cause);
    }
}