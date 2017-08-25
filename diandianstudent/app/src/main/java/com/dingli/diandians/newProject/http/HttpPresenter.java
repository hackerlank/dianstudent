package com.dingli.diandians.newProject.http;
import com.dingli.diandians.common.DianApplication;
import com.dingli.diandians.newProject.constants.BKPreference;
import com.dingli.diandians.newProject.http.base.protocol.BaseProtocol;
import com.dingli.diandians.newProject.http.exception.LoginInvalidException;
import com.dingli.diandians.newProject.http.exception.NetworkConnectionException;
import com.dingli.diandians.newProject.http.exception.RequestIllegalException;
import com.dingli.diandians.newProject.http.exception.ResponseException;
import com.dingli.diandians.newProject.moudle.user.protocol.LoginResultProtocol;
import com.dingli.diandians.newProject.utils.NetUtils;
import com.dingli.diandians.newProject.utils.SPUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.functions.Func1;

/**
 * <p>Title: HttpPresenter<／p>
 * <p>Description: <／p>
 * <p>Copyright: Copyright (c) 2016<／p>
 * <p>Company: utouu<／p>
 *
 * @author dong-chen.wu
 * @version 1.0
 * @date 2017/5/27
 */
public class HttpPresenter {

    /**
     * 最大重试次数
     */
    private static final int MAX_RETRY_COUNT = 2;

    /**
     * 请求非法重试标识
     */
    private static final int REQUEST_ILLEGAL_CODE = -100;

    /**
     * 获取TGT
     */
    private String getTicket() {
        return DianApplication.getInstance().getAuthorization();
    }

    /**
     * 更新ST
     */
    private void setST(String st) {

    }

    /*-----------------------------------------------------*
     |                       Header                        |
     *-----------------------------------------------------*/

    protected Map<String, String> header() {
        return header(new HashMap<>());
    }

    protected Map<String, String> header(String data) {
        HashMap<String, String> params = new HashMap<>();
        params.put("data", data);
        return header(params);
    }

    protected Map<String, String> header(HashMap<String, String> params) {
        long time = System.nanoTime();
        HashMap<String, String> map = new HashMap<>();
        ///   map.put("cas-client-sign", GenerateSignDemo.generateCommonSign(time, params));
        map.put("cas-client-time", String.valueOf(time));
        return map;
    }



    /*-----------------------------------------------------*
     |                        Http                         |
     *-----------------------------------------------------*/

    protected ApiService api() {
        return UtouuHttp.api();
    }

    protected ApiService apiScalars() {
        return UtouuHttpScalars.api();
    }

    protected <T> Observable<BaseProtocol<T>> utouuHttp(Observable<BaseProtocol<T>> observable) {
        return utouuHttp(observable, null, MAX_RETRY_COUNT);
    }

    protected <T> Observable<BaseProtocol<T>> utouuHttp(Observable<BaseProtocol<T>> observable, String requestUrl) {
        return utouuHttp(observable, requestUrl, 0);
    }

    private <T> Observable<BaseProtocol<T>> utouuHttp(final Observable<BaseProtocol<T>> observable, final String requestUrl, int
            currentRetryCount) {

        if (!NetUtils.isConnected( DianApplication.getInstance())) {
            return Observable.error(new NetworkConnectionException());
        }

        Observable<BaseProtocol<T>> responseObservable = responseObservable(observable);
        if (currentRetryCount >= MAX_RETRY_COUNT ) {
            return responseObservable;
        }

        return responseObservable.retryWhen(errors -> errors.flatMap(new Func1<Throwable, Observable<?>>() {
            @Override
            public Observable<?> call(Throwable error) {
                if (error instanceof HttpException && ((HttpException) error).response().code() == 401&&currentRetryCount< MAX_RETRY_COUNT ) {

                    HashMap params = new HashMap();
                    params.put("username", DianApplication.user.account);
                    params.put("password", DianApplication.user.password);
//                    params.put("username",  SPUtils.get(DianApplication.getInstance(), BKPreference.ACCOUNT,""));
//                    params.put("password",  SPUtils.get(DianApplication.getInstance(), BKPreference.PASSWORD,""));
                    params.put("grant_type", "refresh_token");
                  //  params.put("refresh_token",DianApplication.getInstance().getRefreshToken());
                    params.put("refresh_token",DianApplication.user.refresh_token);
                    params.put("scope", "read write");
                    params.put("client_secret", "mySecretOAuthSecret");
                    params.put("client_id", "dleduApp");

                    Call<BaseProtocol<LoginResultProtocol>> call=   api().getToken(params);
                    int retryCount = 1;
                    try {
                        retrofit2.Response<BaseProtocol<LoginResultProtocol>> response = call.execute();
                        if (response.code() == 200) {
                            BaseProtocol<LoginResultProtocol> result = response.body();
                            DianApplication.getInstance().setAuthorization(result.data.token_type + "" + result.data.access_token);//保存tgt
                        }else {
                            return Observable.error(new LoginInvalidException());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return Observable.error(new LoginInvalidException());
                    }
                  //  return utouuHttp(observable.retry(), requestUrl,++retryCount);
                }
                return Observable.error(error);
            }
        }));
    }

    private <T> Observable<BaseProtocol<T>> responseObservable(Observable<BaseProtocol<T>> observable) {
        return observable.flatMap(response -> {
            if (response == null) {
                return Observable.error(new NetworkConnectionException());
            } else if (response.isRequestIllegal()) {
                return Observable.error(new RequestIllegalException());
            } else if (!response.success) {
                return Observable.error(new ResponseException(response));
            }
            return Observable.just(response);
        });
    }

    /*-----------------------------------------------------*
     |                    UploadPhoto                      |
     *-----------------------------------------------------*/

//    protected Observable<String> uploadPhoto(List<String> photos, String url, String paramsName) {
//        return Observable.from(photos)
//                .flatMap(photo -> {
//                            File image = new File(photo);
//                            MultipartBody body = new MultipartBody.Builder()
//                                    .addFormDataPart(paramsName, image.getName(), RequestBody.create(MediaType.parse("image"), image))
//                                    .build();
//                            return utouuHttp(api().uploadPhoto(url, header(), body), url);
//                        }
//                ).flatMap(result -> {
//                    if (result.success) {
//
//                        if (result.data != null && !TextUtils.isEmpty(result.data.url)) {
//                            return Observable.just(result.data.url);
//                        } else if (!TextUtils.isEmpty(result.urls)) {
//                            return Observable.just(result.urls);
//                        } else if (!TextUtils.isEmpty(result.url)) {
//                            return Observable.just(result.url);
//                        } else {
//                            return Observable.just("");
//                        }
//                    } else {
//                        return Observable.error(new ResponseException(result));
//                    }
//
//                });
//    }


}
