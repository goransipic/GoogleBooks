package com.goodapp.googlebooks.ui.booklist;

import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.graphics.Palette;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.goodapp.googlebooks.R;
import com.goodapp.googlebooks.databinding.BookItemBinding;
import com.goodapp.googlebooks.ui.common.DataBoundListAdapter;


/**
 * Created by gsipic on 17.06.17..
 */

public class BookAdapter extends DataBoundListAdapter {

    @Override
    protected int getLayoutIdForPosition(int position) {
        return R.layout.book_item;
    }

    @Override
    protected void bind(ViewDataBinding binding, Object o) {
        super.bind(binding, o);

        BookItemBinding bookItemBinding = (BookItemBinding) binding;
        BookItem obj = (BookItem) o;

        //holder.binding.thumbnail.setAspectRatio(obj.getAspectRatio());

        Glide.with(bookItemBinding.getRoot().getContext() /* context */)
                .load("load something")
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        Bitmap bitmap = ((BitmapDrawable)resource.getCurrent()).getBitmap();
                        Palette palette = Palette.generate(bitmap);
                        int defaultColor = 0xFF333333;
                        int color = palette.getDarkMutedColor(defaultColor);
                        bookItemBinding.cardview.setCardBackgroundColor(color);
                        return false;
                    }
                })
                .into(bookItemBinding.thumbnail);

    }
}
