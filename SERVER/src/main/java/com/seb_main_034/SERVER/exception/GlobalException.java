package com.seb_main_034.SERVER.exception;

import lombok.Getter;

public class GlobalException extends RuntimeException{

    @Getter
    private ExceptionCode code;

    public GlobalException(ExceptionCode code) {
        super(code.getMessage());
        this.code = code;
    }
    public GlobalException() {
        super();
    }

    public GlobalException(String message) {
        super(message);
    }

    public GlobalException(String message, Throwable cause) {
        super(message, cause);
    }

    public GlobalException(Throwable cause) {
        super(cause);
    }

    protected GlobalException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
