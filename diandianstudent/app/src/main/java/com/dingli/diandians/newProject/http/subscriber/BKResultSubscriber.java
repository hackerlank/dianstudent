package com.dingli.diandians.newProject.http.subscriber;
import android.text.TextUtils;

import com.dingli.diandians.newProject.http.exception.ErrorMessageFactory;
import com.dingli.diandians.newProject.http.exception.NetworkConnectionException;
import com.dingli.diandians.newProject.http.exception.RequestIllegalException;
import com.google.gson.JsonSyntaxException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

public abstract class BKResultSubscriber<E> extends Subscriber<E> {

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {
        if (handleResponseError((Exception) e)) {
            return;
        }
        onRequestError((Exception) e);
    }

    protected boolean handleResponseError(Exception e) {
        String message = ErrorMessageFactory.create(e);
        if (e instanceof NetworkConnectionException  || e instanceof SocketTimeoutException) {
            onNetworkFailure(message);
            return true;
        }
        if (e instanceof JsonSyntaxException) {
            onRequestError(new JsonSyntaxException("解析异常"));
            return true;
        }
        if (e instanceof RequestIllegalException) {
            onRequestError(new RequestIllegalException("请求服务器失败"));
            return true;
        }
        if (e instanceof UnknownHostException||e instanceof HttpException) {
            onRequestError(new Exception("服务异常，请稍后再试..."));
            return true;
        }
        if(!TextUtils.isEmpty(message)){
            if (message.contains("443")||message.contains("Failed")) {
                onRequestError(new Exception("服务异常，请稍后再试..."));
                return true;
            }
        }
        return false;
    }

    @Override
    public void onNext(E result) {
        onRequestResult(result);
    }

    protected abstract void onRequestError(Exception e);

    protected abstract void onRequestResult(E result);

    protected abstract void onNetworkFailure(String message);
}
