package com.goodapp.googlebooks.vo;

import com.goodapp.googlebooks.api.response.BookSearchResponse;
import com.goodapp.googlebooks.api.response.Item;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gsipic on 13/01/2018.
 */

public interface BookState {
    BookItemsState reduce(BookItemsState oldState);


    class Loading implements BookState {
        @Override
        public BookItemsState reduce(BookItemsState oldState) {
            return BookItemsState.showLoadingState();
        }
    }

    class InitState implements BookState {
        @Override
        public BookItemsState reduce(BookItemsState oldState) {
            return BookItemsState.showInitState();
        }
    }

    /**
     * Created by gsipic on 14/01/2018.
     */

    class NextPageLoaded implements BookState {

        private final BookSearchResponse bookSearchResponse;

        public NextPageLoaded(BookSearchResponse bookSearchResponse) {
            this.bookSearchResponse = bookSearchResponse;
        }

        @Override
        public BookItemsState reduce(BookItemsState oldState) {

            if (bookSearchResponse == null || bookSearchResponse.getItems() == null) {
                return BookItemsState.showDataState(oldState.getBookSearchResponse());
            }

            List<Item> data = new ArrayList<>(oldState.getBookSearchResponse().getItems().size()
                    + bookSearchResponse.getItems().size());

            data.addAll(oldState.getBookSearchResponse().getItems());
            data.addAll(bookSearchResponse.getItems());

            bookSearchResponse.setItems(data);

            return BookItemsState.showNextPageLoading(bookSearchResponse, false);
        }
    }

    /**
     * Created by gsipic on 14/01/2018.
     */

    class NextPageLoading implements BookState {
        @Override
        public BookItemsState reduce(BookItemsState oldState) {
            return BookItemsState.showNextPageLoading(oldState.getBookSearchResponse(), true);
        }
    }

    class BooksLoaded implements BookState {

        private final BookSearchResponse bookSearchResponse;

        public BooksLoaded(BookSearchResponse bookSearchResponse) {
            this.bookSearchResponse = bookSearchResponse;
        }

        @Override
        public BookItemsState reduce(BookItemsState oldState) {
            return BookItemsState.showDataState(bookSearchResponse);
        }
    }

    class QueryLoaded implements BookState {

        private String[] recentQueriesArray;

        public QueryLoaded(List<RecentQuery> recentQueries) {

            recentQueriesArray = new String[recentQueries.size()];

            for (int i = 0; i < recentQueries.size(); i++) {
                recentQueriesArray[i] = recentQueries.get(i).query;
            }
        }

        @Override
        public BookItemsState reduce(BookItemsState oldState) {
            return BookItemsState.showQueryState(oldState, recentQueriesArray);
        }
    }

    /**
     * Created by gsipic on 13/01/2018.
     */

    class BookError implements BookState {

        public BookError() {
        }

        @Override
        public BookItemsState reduce(BookItemsState oldState) {
            return BookItemsState.showError();
        }
    }
}

