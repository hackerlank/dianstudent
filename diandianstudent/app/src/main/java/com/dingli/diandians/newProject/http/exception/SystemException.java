package com.dingli.diandians.newProject.http.exception;


public class SystemException extends RuntimeException {

    public SystemException() {

    }

    public SystemException(String detailMessage) {
        super(detailMessage);
    }

    public SystemException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public SystemException(Throwable throwable) {
        super(throwable);
    }
}
