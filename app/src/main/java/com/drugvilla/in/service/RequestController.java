package com.drugvilla.in.service;

import android.content.Context;
import android.util.Base64;

import androidx.annotation.NonNull;

import com.drugvilla.in.utils.AppConstant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RequestController {
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";
    private static Retrofit retrofit;
    private static RequestController requestController;
    private static final HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static final OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .readTimeout(3, TimeUnit.MINUTES)
            .connectTimeout(3, TimeUnit.MINUTES);
    private static Interceptor headerInterceptor;

    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    private static final Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(AppConstant.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson));

    public static RequestController getInstance() {
        if (requestController == null) {
            requestController = new RequestController();
        }
        return requestController;
    }

    public static <S> S createService(Context context, Class<S> service) {
        return createService(context, service, true);
    }

    private static <S> S createService(Context context, Class<S> service, boolean isAuth) {
        if (/*headerInterceptor == null && */!okHttpClient.interceptors().contains(headerInterceptor)) {
            if (isAuth) {
                addNetworkInterceptor(context);
            }
        }
        Retrofit retrofit = builder.client(okHttpClient.build()).build();
        return retrofit.create(service);
    }

    private static void addNetworkInterceptor(Context context) {
        String credentials = "admin" + ":" + "admin";
        final String basic = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
        headerInterceptor = new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
                Request original = chain.request();
                Response response;
                Request.Builder builder;
                builder = original.newBuilder()
                        .header(HEADER_CONTENT_TYPE, APPLICATION_JSON)
                        // .header("Authorization",basic)
                        .method(original.method(), original.body());
                response = chain.proceed(builder.build());
                return response;
            }
        };
        okHttpClient.addInterceptor(headerInterceptor);
    }
}
