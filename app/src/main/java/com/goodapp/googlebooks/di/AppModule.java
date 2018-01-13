package com.goodapp.googlebooks.di;

import android.app.Application;

import com.goodapp.googlebooks.api.GoogleApiBooks;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by gsipic on 13/01/2018.
 */

@Module(includes = ViewModelModule.class)
class AppModule {
    @Singleton @Provides
    GoogleApiBooks provideGoogleBooksService() {
        return new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/books/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(GoogleApiBooks.class);
    }
}
