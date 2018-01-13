package com.goodapp.googlebooks.binding;

import android.databinding.DataBindingComponent;
import android.support.v4.app.Fragment;

/**
 * Created by gsipic on 13/01/2018.
 */

public class FragmentDataBindingComponent implements android.databinding.DataBindingComponent {
    private final FragmentBindingAdapters adapter;

    public FragmentDataBindingComponent(Fragment fragment) {
        this.adapter = new FragmentBindingAdapters(fragment);
    }

    @Override
    public FragmentBindingAdapters getFragmentBindingAdapters() {
        return adapter;
    }
}
