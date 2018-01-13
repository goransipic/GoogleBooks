package com.goodapp.googlebooks.vo;

import com.goodapp.googlebooks.api.response.BookSearchResponse;

import java.util.Collections;

/**
 * Created by gsipic on 13/01/2018.
 */

public class BookItemsState {

    private BookSearchResponse bookSearchResponse;

    // True if progressbar should be displayed
    private final boolean loading;

    private final boolean error;

    private final boolean init;

    public BookItemsState(BookSearchResponse bookSearchResponse, boolean loading, boolean error, boolean init) {
        this.bookSearchResponse = bookSearchResponse;
        this.loading = loading;
        this.error = error;
        this.init = init;
    }

    public BookSearchResponse getBookSearchResponse() {
        return bookSearchResponse;
    }

    public boolean isLoading() {
        return loading;
    }

    public boolean isInit() {
        return init;
    }

    public boolean isError() {
        return error;
    }

    public static BookItemsState showLoadingState() {
        return new BookItemsState(null, true, false, false);
    }

    public static BookItemsState showInitState() {
        return new BookItemsState(null, false, false, true);
    }

    public static BookItemsState showDataState(BookSearchResponse bookSearchResponse) {
        return new BookItemsState(bookSearchResponse, false, false, false);
    }

    public static BookItemsState showError() {
        return new BookItemsState(null, false, true, false);
    }
}
