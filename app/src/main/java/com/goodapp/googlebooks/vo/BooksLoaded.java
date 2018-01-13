package com.goodapp.googlebooks.vo;

import com.goodapp.googlebooks.api.response.BookSearchResponse;

public class BooksLoaded implements BookState {

    private final BookSearchResponse bookSearchResponse;

    public BooksLoaded(BookSearchResponse bookSearchResponse) {
        this.bookSearchResponse = bookSearchResponse;
    }

    @Override
    public BookItemsState reduce(BookItemsState oldState) {
        return BookItemsState.showDataState(bookSearchResponse);
    }
}
