package com.dingli.diandians.newProject.http.base.protocol;

import java.io.Serializable;

/**
 * 描述最外层json结构的实体类
 */
public class BaseProtocol<T> implements Serializable {

    private static final String ERROR_CODE_401 = "401";
    private static final String ERROR_CODE_405= "405";
    private static final String ERROR_CODE_400 = "400";
    private static final String ERROR_CODE_030 = "030";

    public T data;
    public String code;
    public String msg;
    public boolean success;

    /**
     * 令牌失效
     */
    public boolean isTokenExpired() {
        return ERROR_CODE_401.equals(code);
    }

    /**
     * 请求是否非法
     */
    public boolean isRequestIllegal() {
        return ERROR_CODE_030.equals(code);
    }
}
