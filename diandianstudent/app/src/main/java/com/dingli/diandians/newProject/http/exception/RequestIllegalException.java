package com.dingli.diandians.newProject.http.exception;

/**
 * 非法请求
 */
public class RequestIllegalException extends RuntimeException {
    public RequestIllegalException() {
    }

    public RequestIllegalException(String message) {
        super(message);
    }
}
