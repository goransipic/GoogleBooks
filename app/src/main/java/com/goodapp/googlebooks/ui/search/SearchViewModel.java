package com.goodapp.googlebooks.ui.search;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.goodapp.googlebooks.repository.BooksRepository;

import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by gsipic on 13/01/2018.
 */

public class SearchViewModel extends ViewModel {



    @Inject
    SearchViewModel(BooksRepository booksRepository) {

    }


    public void setQuery(@NonNull String originalInput) {

    }

    void refresh() {

    }

    public void loadNextPage() {

    }

    public LiveData<Object> getLoadMoreStatus() {
        return null;
    }
}
