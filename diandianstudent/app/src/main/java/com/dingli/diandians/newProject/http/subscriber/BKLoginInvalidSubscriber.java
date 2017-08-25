package com.dingli.diandians.newProject.http.subscriber;

import com.dingli.diandians.newProject.http.exception.LoginInvalidException;
import com.dingli.diandians.newProject.http.exception.TokenExpiredException;

import retrofit2.adapter.rxjava.HttpException;

public abstract class BKLoginInvalidSubscriber<E> extends BKResultSubscriber<E> {

    @Override
    protected boolean handleResponseError(Exception e) {
        if (e instanceof HttpException && ((HttpException) e).response().code() == 401){
            onLoginInvalid("令牌失效请重新登录!");
            return true;
        }
        if (e instanceof LoginInvalidException) {
            onLoginInvalid(e.getMessage());
            return true;
        }
        if (e instanceof TokenExpiredException) {
            onLoginInvalid("令牌失效请重新登录!");
            return true;
        }
        return super.handleResponseError(e);
    }


    protected abstract void onLoginInvalid(String message);
}
