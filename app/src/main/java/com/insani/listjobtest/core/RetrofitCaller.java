package com.insani.listjobtest.core;

import android.content.Context;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.insani.listjobtest.util.exception.NoNetworkException;
import com.insani.listjobtest.util.helper.NetworkHelper;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitCaller {
    private static final String url = "http://dev3.dansmultipro.co.id/api/";
    private static RetrofitCaller instance;

    protected Retrofit clientHttpsCall;

    public static RetrofitCaller getInstance(Context mContext) {
        if (instance == null) {
            instance = new RetrofitCaller(mContext);
        }

        return instance;
    }

    private RetrofitCaller(Context mContext) {
        OkHttpClient.Builder httpClientHttpsCall = new OkHttpClient.Builder();

        httpClientHttpsCall.addInterceptor(chain -> {
            Request originalHttpsCall = chain.request();
            Request.Builder requestBuilder = originalHttpsCall.newBuilder()
                    .header("Cache-Control", "no-cache");
            Request requestHttpsCall = requestBuilder.build();
            return chain.proceed(requestHttpsCall);
        });

        httpClientHttpsCall.addInterceptor(chain -> {
            if (!NetworkHelper.getConnectivityStatus(mContext))
                throw new NoNetworkException();
            return chain.proceed(chain.request().newBuilder().build());
        });

        httpClientHttpsCall.readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.MINUTES)
                .connectTimeout(60, TimeUnit.SECONDS);

        OkHttpClient okHttpClient = httpClientHttpsCall.build();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        clientHttpsCall = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .client(okHttpClient)
                .build();
    }

    public Retrofit getClientHttpsCall() {
        return clientHttpsCall;
    }

    public <T> T create(final Class<T> serviceHttpsCall) {
        return clientHttpsCall.create(serviceHttpsCall);
    }
}

