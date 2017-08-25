package com.dingli.diandians.newProject.http;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * <p>Title: UtouuHttp<／p>
 * <p>Description: <／p>
 * <p>Copyright: Copyright (c) 2016<／p>
 * <p>Company: utouu<／p>
 *
 * @author lwq
 * @version 1.0
 * @date 16/11/2
 */
public final class UtouuHttpScalars extends UtouuClient {

    private static ApiService mApiService;

    public static ApiService api() {
        return null == mApiService ? mApiService = getRetrofit().create(ApiService.class) : mApiService;
    }

    private static Retrofit getRetrofit() {
        return new Retrofit.Builder().client(getClient())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl("http://api.bestkeep.cn")
                .build();
    }
}
