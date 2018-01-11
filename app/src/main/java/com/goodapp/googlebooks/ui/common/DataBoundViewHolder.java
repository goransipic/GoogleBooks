package com.goodapp.googlebooks.ui.common;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

/**
 * Created by gsipic on 17.06.17..
 */

public class DataBoundViewHolder extends RecyclerView.ViewHolder {
    public final ViewDataBinding binding;
    DataBoundViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }
}
