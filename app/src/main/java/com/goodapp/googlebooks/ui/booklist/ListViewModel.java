package com.goodapp.googlebooks.ui.booklist;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.goodapp.googlebooks.repository.ListRepository;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;

/**
 * Created by gsipic on 18.11.17..
 */

public class ListViewModel extends ViewModel {

    private ListRepository listRepository;
    private MutableLiveData<List<BookItem>> mutableLiveData = new MutableLiveData<>();

    Disposable disposable;

    public ListViewModel() {

        this.listRepository = new ListRepository();

        disposable = Observable.merge(listRepository.loadBooks(), listRepository.updatedBooks())
                .subscribe(item -> mutableLiveData.postValue(item));

    }

    @Override
    protected void onCleared() {
        disposable.dispose();
    }

    public LiveData<List<BookItem>> getBooks() {
        return mutableLiveData;
    }

}
