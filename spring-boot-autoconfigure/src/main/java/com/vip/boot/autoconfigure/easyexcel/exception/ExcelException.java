package com.vip.boot.autoconfigure.easyexcel.exception;

import java.io.Serial;

/**
 * @author echo
 * @version 1.0
 * @date 2023/11/18 21:06
 */
public class ExcelException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 2523106933655728813L;

    public ExcelException() {
        super();
    }

    public ExcelException(String message) {
        super(message);
    }

    public ExcelException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExcelException(Throwable cause) {
        super(cause);
    }
}