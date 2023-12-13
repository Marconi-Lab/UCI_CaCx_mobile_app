package com.ug.air.uci_cacx.APIs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiClient {
    private static final String BASE_URL_1 = "https://ucicompute.org/";
    private static final String BASE_URL_2 = "http://104.198.50.209:8008/";
    private static Retrofit retrofit_1 = null;
    private static BasicAuthInterceptor basicAuthInterceptor;
    private static Retrofit retrofit_2 = null;

    public static Retrofit getClient_1(String username, String password) {

        if (retrofit_1 == null){

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            basicAuthInterceptor = new BasicAuthInterceptor(username, password);
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(basicAuthInterceptor)
                    .build();

            retrofit_1 = new Retrofit.Builder()
                    .baseUrl(BASE_URL_1)
                    .client(okHttpClient)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        else {
            basicAuthInterceptor.updateCredentials(username, password);
        }
        return retrofit_1;
    }

    public static Retrofit getClient_2() {

        if (retrofit_2 == null){

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(loggingInterceptor)
                    .build();

            retrofit_2 = new Retrofit.Builder()
                    .baseUrl(BASE_URL_2)
                    .client(okHttpClient)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit_2;
    }
}
