package com.goodapp.googlebooks.ui.search;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import com.goodapp.googlebooks.R;
import com.goodapp.googlebooks.api.response.BookSearchResponse;
import com.goodapp.googlebooks.binding.FragmentDataBindingComponent;
import com.goodapp.googlebooks.databinding.SearchFragmentBinding;
import com.goodapp.googlebooks.di.Injectable;
import com.goodapp.googlebooks.ui.common.BookAdapter;
import com.goodapp.googlebooks.ui.common.NavigationController;
import com.goodapp.googlebooks.util.AutoClearedValue;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;

import javax.inject.Inject;

/**
 * Created by gsipic on 13/01/2018.
 */

public class SearchFragment extends Fragment implements Injectable {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);

    AutoClearedValue<SearchFragmentBinding> binding;

    BookAdapter adapter;

    private SearchViewModel searchViewModel;
    private GridLayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        SearchFragmentBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.search_fragment, container, false,
                        dataBindingComponent);
        binding = new AutoClearedValue<>(this, dataBinding);
        return dataBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchViewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel.class);
        initRecyclerView();
        BookAdapter rvAdapter = new BookAdapter();
        binding.get().bookList.setAdapter(rvAdapter);
        adapter = rvAdapter;

        searchViewModel.render().observe(this, items -> {

                    if (items.isInit()) {
                        binding.get().loadingState.progressBar.setVisibility(View.GONE);
                        binding.get().loadingState.errorMsg.setVisibility(View.GONE);
                        binding.get().loadingState.retry.setVisibility(View.GONE);
                        binding.get().bookList.setVisibility(View.GONE);
                    } else if (items.isError()) {
                        binding.get().loadingState.progressBar.setVisibility(View.GONE);
                        binding.get().loadingState.errorMsg.setVisibility(View.VISIBLE);
                        binding.get().loadingState.retry.setVisibility(View.GONE);
                        binding.get().bookList.setVisibility(View.GONE);

                        binding.get().loadingState.errorMsg.setText(R.string.unknown_error);

                    } else if (items.isLoadingFirstPage()) {
                        binding.get().loadingState.progressBar.setVisibility(View.VISIBLE);
                        binding.get().loadingState.errorMsg.setVisibility(View.GONE);
                        binding.get().loadingState.retry.setVisibility(View.GONE);
                        binding.get().bookList.setVisibility(View.GONE);

                    } else if (items.getBookSearchResponse() != null && items.getBookSearchResponse().getItems() != null && !items.isLoadingNextPage()) {
                        binding.get().loadingState.progressBar.setVisibility(View.GONE);
                        binding.get().loadingState.errorMsg.setVisibility(View.GONE);
                        binding.get().loadingState.retry.setVisibility(View.GONE);
                        binding.get().bookList.setVisibility(View.VISIBLE);

                        BookSearchResponse bookSearchResponse = items.getBookSearchResponse();
                        adapter.setLoadingNextPage(items.isLoadingNextPage());
                        adapter.replace(bookSearchResponse.getItems());

                        binding.get().bookList.scrollBy(0,10);

                    } else if (!items.isLoadingFirstPage()) {
                        boolean changed = adapter.setLoadingNextPage(items.isLoadingNextPage());

                        if (changed && items.isLoadingNextPage()) {
                            // scroll to the end of the list so that the user sees the load more progress bar
                            binding.get().bookList.smoothScrollToPosition(adapter.getItemCount());
                        }

                        adapter.replace(items.getBookSearchResponse().getItems());


                    }
                }
        );

        initSearchInputListener();

        binding.get().setCallback(() -> searchViewModel.refresh());
    }

    private void initSearchInputListener() {
        binding.get().input.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                doSearch(v);
                return true;
            }
            return false;
        });
        binding.get().input.setOnKeyListener((v, keyCode, event) -> {
            if ((event.getAction() == KeyEvent.ACTION_DOWN)
                    && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                doSearch(v);
                return true;
            }
            return false;
        });
    }

    private void doSearch(View v) {
        String query = binding.get().input.getText().toString();
        // Dismiss keyboard
        dismissKeyboard(v.getWindowToken());
        binding.get().setQuery(query);
        searchViewModel.setQuery(query);
    }

    private void initRecyclerView() {
        layoutManager = new GridLayoutManager(this.getContext(), 2);

        binding.get().bookList.setLayoutManager(layoutManager);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {

                int viewType = adapter.getItemViewType(position);
                if (viewType == R.layout.item_loading) {
                    return 2;
                }

                return 1;
            }
        });

        RxRecyclerView.scrollStateChanges(binding.get().bookList)
                .filter(event -> !adapter.isLoadingNextPage())
                .filter(event -> event == RecyclerView.SCROLL_STATE_IDLE)
                .filter(event -> layoutManager.findLastCompletelyVisibleItemPosition()
                        == adapter.getItems().size() - 1)
                .map(integer -> true).subscribe(event -> searchViewModel.loadNextPage());


    }

    private void dismissKeyboard(IBinder windowToken) {
        FragmentActivity activity = getActivity();
        if (activity != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(windowToken, 0);
        }
    }

}
