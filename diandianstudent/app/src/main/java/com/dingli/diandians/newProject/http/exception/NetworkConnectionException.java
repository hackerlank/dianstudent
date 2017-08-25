package com.dingli.diandians.newProject.http.exception;

/**
 * 网络连接异常
 */
public class NetworkConnectionException extends RuntimeException {

    public NetworkConnectionException() {

    }

    public NetworkConnectionException(String message) {
        super(message);
    }
}
