package com.goodapp.googlebooks.api;

import android.arch.lifecycle.LiveData;

import com.goodapp.googlebooks.api.response.BookSearchResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by gsipic on 11/01/2018.
 */

public interface GoogleApiBooks {

    @GET("volumes")
    Observable<BookSearchResponse> getBooks(
            @Query("q") String query,
            @Query("maxResults") Integer maxResults,
            @Query("startIndex") Integer startIndex);
}
