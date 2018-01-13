package com.goodapp.googlebooks.vo;

/**
 * Created by gsipic on 13/01/2018.
 */

public interface BookState {
    BookItemsState reduce(BookItemsState oldState);
}

