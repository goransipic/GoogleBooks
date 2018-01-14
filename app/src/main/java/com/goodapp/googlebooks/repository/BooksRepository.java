package com.goodapp.googlebooks.repository;

import com.goodapp.googlebooks.api.GoogleApiBooks;
import com.goodapp.googlebooks.vo.BookState;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by gsipic on 18.11.17..
 */

@Singleton
public class BooksRepository {

    private String mQuery;
    private final GoogleApiBooks mGoogleApiBooks;
    private int startIndex = 0;
    private static final int MAX_RESULT = 10;
    private static final int START_INDEX_OFFSET = 10;

    @Inject
    public BooksRepository(GoogleApiBooks googleApiBooks) {
        this.mGoogleApiBooks = googleApiBooks;
    }

    public Observable<BookState> getBooks(String query) {
        startIndex = 0;
        mQuery = query;
        return mGoogleApiBooks
                .getBooks(query, MAX_RESULT, startIndex)
                .map(BookState.BooksLoaded::new)
                .cast(BookState.class)
                .onErrorReturn(error -> new BookState.BookError())
                .startWith(new BookState.Loading())
                .subscribeOn(Schedulers.io());
    }

    public Observable<BookState> loadNextPage() {
        return mGoogleApiBooks
                .getBooks(mQuery, MAX_RESULT, startIndex += START_INDEX_OFFSET)
                .map(BookState.NextPageLoaded::new)
                .cast(BookState.class)
                .onErrorReturn(error -> new BookState.BookError())
                .startWith(new BookState.NextPageLoading())
                .subscribeOn(Schedulers.io());
    }

}
