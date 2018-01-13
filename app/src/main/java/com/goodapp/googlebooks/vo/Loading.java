package com.goodapp.googlebooks.vo;

public class Loading implements BookState {
    @Override
    public BookItemsState reduce(BookItemsState oldState) {
        return BookItemsState.showLoadingState();
    }
}
