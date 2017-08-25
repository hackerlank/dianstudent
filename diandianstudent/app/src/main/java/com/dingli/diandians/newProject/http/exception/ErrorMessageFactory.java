package com.dingli.diandians.newProject.http.exception;


import android.content.res.Resources;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.adapter.rxjava.HttpException;

public class ErrorMessageFactory {

    public static String create(Exception exception) {
        String message;
        if (exception instanceof NetworkConnectionException || exception instanceof UnknownHostException || exception instanceof SocketTimeoutException) {
            message = "网络异常，请检查您的网络...";
        } else if (exception instanceof Resources.NotFoundException) {
            message = exception.getMessage();
        } else if (exception instanceof HttpException) {
            message = exception.getMessage();
        } else if (exception instanceof ResponseException) {
            message = exception.getMessage();
        } else {
            message = exception.getMessage();
        }
        return message;
    }

}
