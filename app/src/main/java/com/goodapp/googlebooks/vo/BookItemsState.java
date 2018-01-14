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

    private final boolean loadingNextPage;

    public BookItemsState(BookSearchResponse bookSearchResponse, boolean loading, boolean loadingNextPage, boolean error, boolean init) {
        this.bookSearchResponse = bookSearchResponse;
        this.loading = loading;
        this.error = error;
        this.init = init;
        this.loadingNextPage = loadingNextPage;
    }

    public BookSearchResponse getBookSearchResponse() {
        return bookSearchResponse;
    }

    public boolean isLoadingFirstPage() {
        return loading;
    }

    public boolean isInit() {
        return init;
    }

    public boolean isError() {
        return error;
    }

    public boolean isLoadingNextPage() {
        return loadingNextPage;
    }

    public static BookItemsState showLoadingState() {
        return new BookItemsState(null, true, false, false, false);
    }

    public static BookItemsState showNextPageLoading(BookSearchResponse bookSearchResponse, boolean loadingNextPage) {
        return new BookItemsState(bookSearchResponse, false, loadingNextPage, false, false);
    }

    public static BookItemsState showInitState() {
        return new BookItemsState(null, false, false, false, true);
    }

    public static BookItemsState showDataState(BookSearchResponse bookSearchResponse) {
        return new BookItemsState(bookSearchResponse, false, false, false, false);
    }

    public static BookItemsState showError() {
        return new BookItemsState(null, false, false, true, false);
    }
}
