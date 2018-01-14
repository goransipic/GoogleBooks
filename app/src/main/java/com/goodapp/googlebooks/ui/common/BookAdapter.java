package com.goodapp.googlebooks.ui.common;

import android.databinding.ViewDataBinding;
import android.databinding.generated.callback.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.goodapp.googlebooks.R;
import com.goodapp.googlebooks.api.response.ImageLinks;
import com.goodapp.googlebooks.api.response.Item;
import com.goodapp.googlebooks.databinding.BookItemBinding;
import com.goodapp.googlebooks.databinding.ItemLoadingBinding;


import java.util.List;


/**
 * Created by gsipic on 17.06.17..
 */

public class BookAdapter extends DataBoundListAdapter {

    private boolean isLoadingNextPage = false;

    private Action action;

    public BookAdapter(Action action) {
        this.action = action;
    }

    @Override
    protected int getLayoutIdForPosition(int position) {

        if (isLoadingNextPage && position == items.size()) {
            return R.layout.item_loading;
        } else {
            return R.layout.book_item;
        }
    }

    @Override
    protected void bind(ViewDataBinding binding, int position) {

        if (binding instanceof ItemLoadingBinding){
            return;
        }

        if (binding instanceof BookItemBinding) {
            BookItemBinding bookItemBinding = (BookItemBinding) binding;
            bookItemBinding.cardview.setOnClickListener(v -> action.onClicked((Item) items.get(position)));
            Item obj = (Item) items.get(position);
            bookItemBinding.articleTitle.setText(obj.getVolumeInfo().getTitle());
            bookItemBinding.articleSubtitle.setText(obj.getVolumeInfo().getPublisher());
            bookItemBinding.articleAuthor.setText(obj.getVolumeInfo().getAuthors() != null ? obj.getVolumeInfo().getAuthors().get(0) : "");
            //holder.binding.thumbnail.setAspectRatio(obj.getAspectRatio());
            bookItemBinding.executePendingBindings();

            ImageLinks imageLinks = obj.getVolumeInfo().getImageLinks();

            if (imageLinks != null && imageLinks.getThumbnail() != null)
                Glide.with(bookItemBinding.getRoot().getContext() /* context */)
                        .load(obj.getVolumeInfo().getImageLinks().getThumbnail())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                Bitmap bitmap = ((BitmapDrawable) resource.getCurrent()).getBitmap();
                                Palette palette = Palette.generate(bitmap);
                                int defaultColor = 0xFF333333;
                                int color = palette.getDarkMutedColor(defaultColor);
                                bookItemBinding.cardview.setCardBackgroundColor(color);
                                return false;
                            }
                        })
                        .into(bookItemBinding.thumbnail);
        } else {
            return;
        }

    }

    public List<?> getItems() {
        return items;
    }

    @Override public int getItemCount() {
        return items == null ? 0 : (items.size() + (isLoadingNextPage ? 1 : 0));
    }

    /**
     * @return true if value has changed since last invocation
     */
    public boolean setLoadingNextPage(boolean loadingNextPage) {
        boolean hasLoadingMoreChanged = loadingNextPage != isLoadingNextPage;

        boolean notifyInserted = loadingNextPage && hasLoadingMoreChanged;
        boolean notifyRemoved = !loadingNextPage && hasLoadingMoreChanged;
        isLoadingNextPage = loadingNextPage;

        if (notifyInserted) {
            notifyItemInserted(items.size());
        } else if (notifyRemoved) {
            notifyItemRemoved(items.size());
        }

        return hasLoadingMoreChanged;
    }

    public boolean isLoadingNextPage() {
        return isLoadingNextPage;
    }

   public interface Action  {
        void onClicked (Item item);
    }
}
