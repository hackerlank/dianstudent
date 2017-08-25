package com.dingli.diandians.newProject.http;
import android.os.Build;
import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.dingli.diandians.BuildConfig;
import com.dingli.diandians.common.DianApplication;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author LWQ
 * @version 1.0
 */
public class UtouuClient {

    private static final int DEFAULT_TIMEOUT = 30;
    private static final String HTTP_ACCEPT = "text/html;charset=UTF-8,application/json";
    private static final String HTTP_CONTENT_TYPE = "application/x-www-form-urlencoded;charset=UTF-8";
    private static final String HTTP_CONNECTION = "close";
    private static final String HTTP_ENCODING = "UTF-8";
    private static String getUserAgent() {
        String httpAgent = System.getProperty("http.agent", Build.FINGERPRINT);
        final int indexOf = httpAgent.indexOf(" ");
        if (indexOf != -1) {
            httpAgent = httpAgent.substring(indexOf);
        }
        return "BESTKEEP" + File.separator + BuildConfig.VERSION_NAME + httpAgent;
    }

    private static Interceptor provideInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("Accept", HTTP_ACCEPT)
                        .addHeader("Content-Type", HTTP_CONTENT_TYPE)
                        .addHeader("Encoding", HTTP_ENCODING)
                        .addHeader("Connection", HTTP_CONNECTION)
//                    .addHeader("User-Agent", getUserAgent())
                        .addHeader("cas-client-service", chain.request().url().toString())
                        .addHeader("Authorization",  DianApplication.getInstance().getAuthorization())
                        .build();
//                Log.d("OkHttp", "<-- request " + request.body().toString());
                Response response = chain.proceed(request);
                okhttp3.MediaType mediaType = response.body().contentType();
                String result = response.body().string();
                JsonObject jsonObject = new JsonObject();
                if (response.code() == 200) {
                    jsonObject.addProperty("success", true);
                    jsonObject.add("data", new JsonParser().parse(result));
                } else {
                    jsonObject.addProperty("success", false);
                    jsonObject.addProperty("data", "{}");
                }
                jsonObject.addProperty("code", response.code() + "");
                String resultJson = jsonObject.toString();

                if (BuildConfig.DIANSTUDENT_DEV || BuildConfig.DIANSTUDENT_TEST) {
                    Log.d("OkHttp", "<-- RESULT " + resultJson);
                }
                return response.newBuilder()
                        .body(okhttp3.ResponseBody.create(mediaType, resultJson))
                        .build();
            }
        };
    }

    protected static OkHttpClient getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        if (BuildConfig.DIANSTUDENT_TEST||BuildConfig.DIANSTUDENT_DEV ||BuildConfig.DIANSTUDENT_FALSELIVE ){
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        }

        return new OkHttpClient.Builder()
                .addInterceptor(provideInterceptor())
                .addInterceptor(logging)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .cookieJar(new CookieJar() {
                    private final HashMap<HttpUrl, List<Cookie>> cookieStore = new HashMap<>();

                    @Override
                    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                        cookieStore.put(HttpUrl.parse(url.host()), cookies);
                    }

                    @Override
                    public List<Cookie> loadForRequest(HttpUrl url) {
                        List<Cookie> cookies = cookieStore.get(HttpUrl.parse(url.host()));
                        return cookies != null ? cookies : new ArrayList<>();
                    }
                })
                .build();
    }
}
