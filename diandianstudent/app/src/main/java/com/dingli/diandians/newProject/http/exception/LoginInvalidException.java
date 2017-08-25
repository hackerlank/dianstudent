package com.dingli.diandians.newProject.http.exception;


/**
 * 登录失效
 * 返回错误码为023 025时候调用
 */
public class LoginInvalidException extends RuntimeException {

    @Override
    public String getMessage() {
        return "令牌失效请重新登录!";
    }
}
