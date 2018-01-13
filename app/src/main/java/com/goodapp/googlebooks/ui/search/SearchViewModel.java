package com.goodapp.googlebooks.ui.search;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import com.goodapp.googlebooks.api.response.Item;
import com.goodapp.googlebooks.repository.BooksRepository;


import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by gsipic on 13/01/2018.
 */

public class SearchViewModel extends ViewModel {

    Subject<String> searchQuery = PublishSubject.create();
    MutableLiveData<List<Item>> result = new MutableLiveData<>();
    DisposableObserver<List<Item>> mObservable;

    @Inject
    SearchViewModel(BooksRepository booksRepository) {

        Observable<List<Item>> observable = searchQuery.switchMap(booksRepository::getBooks);

       mObservable = Observable.merge(observable,observable).subscribeWith(new DisposableObserver<List<Item>>() {
            @Override
            public void onNext(List<Item> items) {
                result.postValue(items);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }

    @Override
    protected void onCleared() {
        mObservable.dispose();
    }

    public LiveData<List<Item>> render() {
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
