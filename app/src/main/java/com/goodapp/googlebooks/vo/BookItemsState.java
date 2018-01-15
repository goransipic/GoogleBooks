package com.goodapp.googlebooks.vo;

import com.goodapp.googlebooks.api.response.BookSearchResponse;

import java.lang.reflect.Array;
import java.util.Collections;
import java.util.List;

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

    private final String[] recentQueries;

    public BookItemsState(BookSearchResponse bookSearchResponse, boolean loading, boolean loadingNextPage, boolean error, boolean init, String[] recentQueries) {
        this.bookSearchResponse = bookSearchResponse;
        this.loading = loading;
        this.error = error;
        this.init = init;
        this.loadingNextPage = loadingNextPage;
        this.recentQueries = recentQueries;
    }

    public String[] getRecentQueries() {
        return recentQueries;
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
        return new BookItemsState(null, true, false, false, false, null);
    }

    public static BookItemsState showNextPageLoading(BookSearchResponse bookSearchResponse, boolean loadingNextPage) {
        return new BookItemsState(bookSearchResponse, false, loadingNextPage, false, false, null);
    }

    public static BookItemsState showInitState() {
        return new BookItemsState(null, false, false, false, true, null);
    }

    public static BookItemsState showDataState(BookSearchResponse bookSearchResponse) {
        return new BookItemsState(bookSearchResponse, false, false, false, false, null);
    }

    public static BookItemsState showError() {
        return new BookItemsState(null, false, false, true, false, null);
    }

    public static BookItemsState showQueryState(BookItemsState oldState, String[] recentQueries) {
        return new BookItemsState(oldState.getBookSearchResponse(), oldState.loading, oldState.loadingNextPage, oldState.error, oldState.init, recentQueries);
    }
}
