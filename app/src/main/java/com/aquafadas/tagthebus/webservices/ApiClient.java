package com.aquafadas.tagthebus.webservices;

import com.aquafadas.tagthebus.utils.AppStaticValues;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Oussama on 20/05/2017.
 */

public class ApiClient {

    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient
                .Builder()
                .readTimeout(10, TimeUnit.MINUTES)
                .connectTimeout(10, TimeUnit.MINUTES)
                .writeTimeout(10, TimeUnit.MINUTES)
                .addInterceptor(interceptor)
                .build();

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(AppStaticValues.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
