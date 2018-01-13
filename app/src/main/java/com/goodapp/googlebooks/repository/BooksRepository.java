package com.goodapp.googlebooks.repository;

import com.goodapp.googlebooks.api.GoogleApiBooks;
import com.goodapp.googlebooks.api.response.BookSearchResponse;
import com.goodapp.googlebooks.api.response.Item;
import com.goodapp.googlebooks.vo.BookItem;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gsipic on 18.11.17..
 */

@Singleton
public class BooksRepository {


    private final GoogleApiBooks mGoogleApiBooks;

    @Inject
    public BooksRepository(GoogleApiBooks googleApiBooks) {
        this.mGoogleApiBooks = googleApiBooks;
    }

    public Observable<List<Item>> getBooks(String query) {
       return mGoogleApiBooks
                .getBooks(query)
                .map(bookSearchResponse -> bookSearchResponse.getItems())
                .subscribeOn(Schedulers.io())
                .onErrorReturn(throwable -> Collections.emptyList())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<List<BookItem>> updatedBooks() {
        return null;
    }

}
