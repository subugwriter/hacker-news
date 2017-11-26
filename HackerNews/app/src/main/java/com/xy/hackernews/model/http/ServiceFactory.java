package com.xy.hackernews.model.http;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xy on 25/11/2017.
 */
public class ServiceFactory {

    private static HackerNewsService mHackerNewsService;

    private static synchronized HackerNewsService initInstance() {
        if (mHackerNewsService == null) {
            final Retrofit retrofit = createRetrofit(HackerNewsService.BASE_URL);
            mHackerNewsService = retrofit.create(HackerNewsService.class);
        }
        return mHackerNewsService;
    }

    /**
     * The HackerNewsService communicates with the Hacker News APIs.
     */
    public static HackerNewsService getHackerNewsService() {
        return initInstance();
    }

    /**
     * Creates a retrofit
     *
     * @param baseUrl REST base url
     * @return retrofit service with defined base url
     */
    private static Retrofit createRetrofit(final String baseUrl) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                //Log.d("HTTP", message);
            }
        });
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .build();

        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) // <- add this
                .client(okHttpClient)
                .build();
    }
}
