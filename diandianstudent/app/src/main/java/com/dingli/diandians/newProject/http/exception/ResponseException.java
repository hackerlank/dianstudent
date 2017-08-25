package com.dingli.diandians.newProject.http.exception;


import android.text.TextUtils;

import com.dingli.diandians.newProject.http.base.protocol.BaseProtocol;


/**
 * 所有response非success的情况会抛出此异常
 * 在此根据code判断是什么类型的错误
 */
public class ResponseException extends RuntimeException {

    private BaseProtocol response;

    public <T> ResponseException(BaseProtocol<T> response) {
        this.response = response;
    }

    public BaseProtocol getResponse() {
        return response;
    }

    @Override
    public String getMessage() {
        if (response != null) {
            if(!TextUtils.isEmpty(response.msg)){
                return response.msg;
            }else {
                return "请求失败";
            }
        }
        return super.getMessage();
    }
}
