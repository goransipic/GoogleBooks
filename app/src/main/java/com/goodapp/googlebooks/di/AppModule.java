package com.goodapp.googlebooks.di;

import android.app.Application;

import com.goodapp.googlebooks.BuildConfig;
import com.goodapp.googlebooks.api.GoogleApiBooks;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.IOException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gsipic on 13/01/2018.
 */

@Module(includes = ViewModelModule.class)
class AppModule {

    @Singleton
    @Provides
    GoogleApiBooks provideGoogleBooksService(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/books/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build()
                .create(GoogleApiBooks.class);
    }

    @Provides
    OkHttpClient provideHttpClient() {

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();

            HttpUrl originalHttpUrl = original.url();
            HttpUrl url = originalHttpUrl.newBuilder()
                    .addQueryParameter("key", "AIzaSyDnetujB0jerBIiviykhHL_1OVI37aNuug")
                    .build();

            Request.Builder requestBuilder = original.newBuilder()
                    .url(url);

            Request request = requestBuilder.build();

            return chain.proceed(request);
        }).addInterceptor(httpLoggingInterceptor);

        return httpClient.build();

    }

}
