package com.goodapp.googlebooks.ui.search;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.goodapp.googlebooks.repository.BooksRepository;
import com.goodapp.googlebooks.vo.BookItemsState;
import com.goodapp.googlebooks.vo.BookState;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by gsipic on 13/01/2018.
 */

public class SearchViewModel extends ViewModel {

    Subject<String> searchQuery = PublishSubject.create();
    MutableLiveData<BookItemsState> result = new MutableLiveData<>();

    Disposable mDisposable;

    @Inject
    SearchViewModel(BooksRepository booksRepository) {
        Observable<BookState> observable = searchQuery.switchMap(booksRepository::getBooks);

        // Show Loading as inital state
        BookItemsState initialState = BookItemsState.showInitState();

        mDisposable = observable
                .scan(initialState, (oldBookState, newBookState) -> newBookState.reduce(oldBookState))
                .subscribe(items -> result.postValue(items));
    }

    @Override
    protected void onCleared() {
        mDisposable.dispose();
    }

    public LiveData<BookItemsState> render() {
        return result;
    }

    public void setQuery(@NonNull String originalInput) {
        searchQuery.onNext(originalInput);
    }

    void refresh() {

    }

    public void loadNextPage() {

    }

    public LiveData<Object> getLoadMoreStatus() {
        return null;
    }
}
