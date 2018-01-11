package com.goodapp.googlebooks.ui.common;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.goodapp.googlebooks.BR;

import java.util.List;

/**
 * Created by gsipic on 11/01/2018.
 */

public abstract class DataBoundListAdapter
        extends RecyclerView.Adapter<DataBoundViewHolder> {

    @NonNull
    private List<?> items;
    private int dataVersion = 0;

    @Override
    public final DataBoundViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = createBinding(parent,viewType);
        return new DataBoundViewHolder(binding);
    }

    protected ViewDataBinding createBinding(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()), viewType,
                        parent, false);
        return binding;
    }

    @Override
    public final void onBindViewHolder(DataBoundViewHolder holder, int position) {
        bind(holder.binding, items.get(position));
        holder.binding.executePendingBindings();
    }

    protected void bind(ViewDataBinding binding, Object o) {
        binding.setVariable(BR.obj,o);
    }

    @SuppressLint("StaticFieldLeak")
    @MainThread
    public void replace(final List<?> update) {
        dataVersion ++;
        if (items == null) {
            if (update == null) {
                return;
            }
            items = update;
            notifyDataSetChanged();
        } else if (update == null) {
            int oldSize = items.size();
            items = null;
            notifyItemRangeRemoved(0, oldSize);
        } else {
            final int startVersion = dataVersion;
            final List  oldItems = items;
            /*new AsyncTask<Void, Void, DiffUtil.DiffResult>() {
                @Override
                protected DiffUtil.DiffResult  doInBackground(Void... voids) {
                    return DiffUtil.calculateDiff(new DiffUtil.Callback() {
                        @Override
                        public int getOldListSize() {
                            return oldItems.size();
                        }

                        @Override
                        public int getNewListSize() {
                            return update.size();
                        }

                        @Override
                        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                            T oldItem = oldItems.get(oldItemPosition);
                            T newItem = update.get(newItemPosition);
                            return DataBoundListAdapter.this.areItemsTheSame(oldItem, newItem);
                        }

                        @Override
                        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                            T oldItem = oldItems.get(oldItemPosition);
                            T newItem = update.get(newItemPosition);
                            return DataBoundListAdapter.this.areContentsTheSame(oldItem, newItem);
                        }
                    });
                }

                @Override
                protected void onPostExecute(DiffUtil.DiffResult diffResult) {
                    if (startVersion != dataVersion) {
                        // ignore update
                        return;
                    }
                    items = update;
                    diffResult.dispatchUpdatesTo(DataBoundListAdapter.this);

                }
            }.execute();*/
        }
    }

    protected abstract int getLayoutIdForPosition(int position);

    @Override
    public int getItemCount() {return items == null ? 0 : items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }
}
