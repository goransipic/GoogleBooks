package com.goodapp.googlebooks.vo;

/**
 * Created by gsipic on 13/01/2018.
 */

public class BookError implements BookState {

    public BookError() {
    }

    @Override
    public BookItemsState reduce(BookItemsState oldState) {
        return BookItemsState.showError();
    }
}
