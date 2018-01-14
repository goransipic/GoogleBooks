package com.goodapp.googlebooks.ui.search;

import android.annotation.TargetApi;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import com.goodapp.googlebooks.R;
import com.goodapp.googlebooks.api.response.BookSearchResponse;
import com.goodapp.googlebooks.api.response.Item;
import com.goodapp.googlebooks.binding.FragmentDataBindingComponent;
import com.goodapp.googlebooks.databinding.SearchFragmentBinding;
import com.goodapp.googlebooks.di.Injectable;
import com.goodapp.googlebooks.ui.common.BookAdapter;
import com.goodapp.googlebooks.ui.common.NavigationController;
import com.goodapp.googlebooks.util.AutoClearedValue;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

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

    SearchFragmentBinding binding;

    BookAdapter adapter;

    private SearchViewModel searchViewModel;
    private GridLayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        SearchFragmentBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.search_fragment, container, false,
                        dataBindingComponent);
        binding = dataBinding;
        return dataBinding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        binding.included.searchView.setMenuItem(item);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        searchViewModel = ViewModelProviders.of(this, viewModelFactory).get(SearchViewModel.class);
        initRecyclerView();
        initSearchView();

        searchViewModel.render().observe(this, items -> {

                    if (items.isInit()) {
                        binding.loadingState.progressBar.setVisibility(View.GONE);
                        binding.loadingState.infoMsg.setVisibility(View.VISIBLE);
                        binding.loadingState.infoMsg.setCompoundDrawablesWithIntrinsicBounds(null,null,null,ContextCompat.getDrawable(getActivity(),R.drawable.search_icon));
                        binding.loadingState.retry.setVisibility(View.GONE);
                        binding.bookList.setVisibility(View.GONE);

                        binding.loadingState.infoMsg.setText("Click icon for Search");

                    } else if (items.isError()) {
                        binding.loadingState.progressBar.setVisibility(View.GONE);
                        binding.loadingState.infoMsg.setVisibility(View.VISIBLE);
                        binding.loadingState.retry.setVisibility(View.GONE);
                        binding.bookList.setVisibility(View.GONE);
                        binding.loadingState.infoMsg.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
                        binding.loadingState.infoMsg.setText(R.string.unknown_error);

                    } else if (items.isLoadingFirstPage()) {
                        binding.loadingState.progressBar.setVisibility(View.VISIBLE);
                        binding.loadingState.infoMsg.setVisibility(View.GONE);
                        binding.loadingState.retry.setVisibility(View.GONE);
                        binding.bookList.setVisibility(View.GONE);

                    } else if (items.getBookSearchResponse() != null && items.getBookSearchResponse().getItems() != null && !items.isLoadingNextPage()) {
                        binding.loadingState.progressBar.setVisibility(View.GONE);
                        binding.loadingState.infoMsg.setVisibility(View.GONE);
                        binding.loadingState.retry.setVisibility(View.GONE);
                        binding.bookList.setVisibility(View.VISIBLE);

                        BookSearchResponse bookSearchResponse = items.getBookSearchResponse();
                        adapter.setLoadingNextPage(items.isLoadingNextPage());
                        adapter.replace(bookSearchResponse.getItems());

                    } else if (!items.isLoadingFirstPage()) {
                        boolean changed = adapter.setLoadingNextPage(items.isLoadingNextPage());

                        if (changed && items.isLoadingNextPage()) {
                            // scroll to the end of the list so that the user sees the load more progress bar
                            binding.bookList.smoothScrollToPosition(adapter.getItemCount());
                        }

                        adapter.replace(items.getBookSearchResponse().getItems());
                    }
                }
        );

        binding.setCallback(() -> searchViewModel.refresh());

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((AppCompatActivity) getActivity()).setSupportActionBar(binding.included.toolbar);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initSearchView() {
        MaterialSearchView searchView = binding.included.searchView;
        searchView.setHint(getString(R.string.search_hint_text));
        searchView.setHintTextColor(R.color.colorAccent);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewModel.setQuery(query);
                binding.included.searchView.hideKeyboard(searchView);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getActivity().getWindow();
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.material_gray_500));
                }
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Window window = getActivity().getWindow();
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
                }
            }
        });

    }

    private void initRecyclerView() {

        BookAdapter rvAdapter = new BookAdapter(item -> {
            navigationController.navigateToDetail(item);

        });
        binding.bookList.setAdapter(rvAdapter);

        adapter = rvAdapter;

        layoutManager = new GridLayoutManager(this.getContext(), 2);

        binding.bookList.setLayoutManager(layoutManager);

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

        RxRecyclerView.scrollStateChanges(binding.bookList)
                .filter(event -> !adapter.isLoadingNextPage())
                .filter(event -> event == RecyclerView.SCROLL_STATE_IDLE)
                .filter(event -> layoutManager.findLastCompletelyVisibleItemPosition()
                        == adapter.getItems().size() - 1)
                .map(integer -> true).subscribe(event -> searchViewModel.loadNextPage());


    }

}
