package com.goodapp.googlebooks.di;

import com.goodapp.googlebooks.ui.detail.DetailFragment;
import com.goodapp.googlebooks.ui.search.SearchFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by gsipic on 13/01/2018.
 */

@Module
public abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract SearchFragment contributeSearchFragment();

    @ContributesAndroidInjector
    abstract DetailFragment contributeDetailFragment();
}
