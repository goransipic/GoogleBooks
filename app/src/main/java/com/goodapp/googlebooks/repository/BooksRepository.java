package com.goodapp.googlebooks.repository;

import com.goodapp.googlebooks.api.GoogleApiBooks;
import com.goodapp.googlebooks.ui.search.BookItem;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

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

    public Observable<List<BookItem>> loadBooks() {

        return null;
    }

    public Observable<List<BookItem>> updatedBooks() {

        return null;
    }

}
