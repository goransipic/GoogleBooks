package com.goodapp.googlebooks.repository;

import com.goodapp.googlebooks.api.GoogleApiBooks;
import com.goodapp.googlebooks.db.BooksDb;
import com.goodapp.googlebooks.vo.BookState;
import com.goodapp.googlebooks.vo.RecentQuery;

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
    private final BooksDb mBooksDb;
    private int startIndex = 0;
    private static final int MAX_RESULT = 10;
    private static final int START_INDEX_OFFSET = 10;

    @Inject
    public BooksRepository(BooksDb booksDb, GoogleApiBooks googleApiBooks) {
        this.mGoogleApiBooks = googleApiBooks;
        this.mBooksDb = booksDb;
    }

    public Observable<BookState> getBooks(String query) {
        mQuery = query;

        return mGoogleApiBooks.getBooks(query, MAX_RESULT, 0)
                .map(BookState.BooksLoaded::new)
                .cast(BookState.class)
                .doOnNext(item -> mBooksDb.queryDao().insert(new RecentQuery(query)))
                .onErrorReturn(error -> new BookState.BookError())
                .startWith(new BookState.Loading())
                .doOnNext(item -> startIndex = 0)
                .subscribeOn(Schedulers.io());
    }

    public Observable<BookState> loadNextPage() {
        return mGoogleApiBooks.getBooks(mQuery, MAX_RESULT, startIndex += START_INDEX_OFFSET)
                .map(BookState.NextPageLoaded::new)
                .cast(BookState.class)
                .onErrorReturn(error -> new BookState.BookError())
                .startWith(new BookState.NextPageLoading())
                .subscribeOn(Schedulers.io());
    }

    public Observable<BookState> getRecentQuery() {
        return mBooksDb.queryDao()
                .getRecentQuery()
                .filter(item -> item.size() > 0)
                .map(BookState.QueryLoaded::new)
                .cast(BookState.class)
                .subscribeOn(Schedulers.io())
                .toObservable();
    }


}
