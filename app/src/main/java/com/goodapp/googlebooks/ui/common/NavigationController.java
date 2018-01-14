package com.goodapp.googlebooks.ui.common;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import com.goodapp.googlebooks.MainActivity;
import com.goodapp.googlebooks.R;
import com.goodapp.googlebooks.api.response.Item;
import com.goodapp.googlebooks.ui.detail.DetailFragment;
import com.goodapp.googlebooks.ui.search.SearchFragment;

import javax.inject.Inject;

/**
 * Created by gsipic on 13/01/2018.
 */

public class NavigationController {
    private final int containerId;
    private final FragmentManager fragmentManager;

    @Inject
    public NavigationController(MainActivity mainActivity) {
        this.containerId = R.id.container;
        this.fragmentManager = mainActivity.getSupportFragmentManager();
    }

    public void navigateToSearch() {
        SearchFragment searchFragment = new SearchFragment();
        fragmentManager.beginTransaction()
                .replace(containerId, searchFragment)
                .commitAllowingStateLoss();
    }

    public void navigateToDetail(Item item) {
        DetailFragment detailFragment = new DetailFragment();

        Bundle bundle = new Bundle();

        String title = item.getVolumeInfo() != null ? item.getVolumeInfo().getTitle() != null ? item.getVolumeInfo().getTitle() : "" : "";
        String publisher = item.getVolumeInfo() != null ? item.getVolumeInfo().getPublisher() != null ? item.getVolumeInfo().getPublisher() : "" : "";
        String author = item.getVolumeInfo() != null ? item.getVolumeInfo().getAuthors() != null ? item.getVolumeInfo().getAuthors().get(0) : "" : "";
        String imageUrl = item.getVolumeInfo() != null ? item.getVolumeInfo().getImageLinks() != null ? item.getVolumeInfo().getImageLinks().getThumbnail() : "" :"";
        String description = item.getVolumeInfo() != null ? item.getVolumeInfo().getDescription() != null ? item.getVolumeInfo().getDescription() : "" : "";


        bundle.putString(DetailFragment.ARG_TITLE, title);
        bundle.putString(DetailFragment.ARG_PUBLISHER, publisher);
        bundle.putString(DetailFragment.ARG_AUTHORS, author);
        bundle.putString(DetailFragment.ARG_IMAGE_URL, imageUrl);
        bundle.putString(DetailFragment.ARG_BODY, description);

        detailFragment.setArguments(bundle);

        fragmentManager.beginTransaction()
                .replace(containerId, detailFragment)
                .addToBackStack(null)
                .commitAllowingStateLoss();

    }
}
