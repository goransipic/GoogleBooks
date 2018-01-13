package com.goodapp.googlebooks.ui.common;

import android.support.v4.app.FragmentManager;

import com.goodapp.googlebooks.MainActivity;
import com.goodapp.googlebooks.R;
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
}
