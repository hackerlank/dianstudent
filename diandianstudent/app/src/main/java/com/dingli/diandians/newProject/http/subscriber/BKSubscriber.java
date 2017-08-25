package com.dingli.diandians.newProject.http.subscriber;


import android.text.TextUtils;

/**
 * 需要拿到请求结果做界面展示的订阅者
 * 包含了网络不可用，登录失效的判断
 * -仅仅判断了View是否存在的情况
 * -View不可用将导致结果不被处理：return
 */
public abstract class BKSubscriber<E> extends BKLoginInvalidSubscriber<E> {

    @Override
    protected void onLoginInvalid(String message) {
    }
    @Override
    protected void onRequestError(Exception e) {
        if(!TextUtils.isEmpty(e.getMessage())&&e.getMessage().contains("http")){
            onFailure("服务异常，请稍后再试...");
        }else {
            onFailure(e.getMessage());
        }
    }
    @Override
    protected void onRequestResult(E result) {
        onSuccess(result);
    }
    protected abstract void onSuccess(E result);
    protected abstract void onFailure(String message);
    @Override
    protected void onNetworkFailure(String message) {

    }
}