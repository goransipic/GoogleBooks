package com.goodapp.googlebooks.ui.detail;

import android.content.Intent;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.goodapp.googlebooks.R;
import com.goodapp.googlebooks.binding.FragmentDataBindingComponent;
import com.goodapp.googlebooks.databinding.DetailFragmentBinding;
import com.goodapp.googlebooks.di.Injectable;
import com.goodapp.googlebooks.ui.common.DynamicHeightNetworkImageView;

/**
 * Created by gsipic on 14/01/2018.
 */

public class DetailFragment extends Fragment implements Injectable {

    public static final String ARG_TITLE = "ARG_TITLE";
    public static final String ARG_PUBLISHER = "ARG_PUBLISHER";
    public static final String ARG_AUTHORS = "AUTHORS";
    public static final String ARG_IMAGE_URL = "ARG_IMAGE_URL";
    public static final String ARG_BODY = "ARG_BODY";
    public static final String ARG_PUBLISHED_DATE = "ARG_PUBLISHED_DAYS";

    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private DynamicHeightNetworkImageView mPhotoView;
    private int mMutedColor = 0xFF333333;
    private View mRootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        DetailFragmentBinding dataBinding = DataBindingUtil
                .inflate(inflater, R.layout.detail_fragment, container, false,
                        dataBindingComponent);
        mRootView = dataBinding.getRoot();
        dataBinding.actionUp.setOnClickListener(view -> getFragmentManager().popBackStack());

        mPhotoView = mRootView.findViewById(R.id.photo);

        mRootView.findViewById(R.id.share_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                        .setType("text/plain")
                        .setText("Some sample text")
                        .getIntent(), getString(R.string.action_share)));
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(getActivity(), R.color.colorPrimaryDark));
        }

        bindViews();
        return dataBinding.getRoot();
    }

    private void bindViews() {
        if (mRootView == null) {
            return;
        }

        TextView titleView = (TextView) mRootView.findViewById(R.id.article_title);
        TextView bylineView = (TextView) mRootView.findViewById(R.id.article_byline);
        bylineView.setMovementMethod(new LinkMovementMethod());
        TextView bodyView = (TextView) mRootView.findViewById(R.id.article_body);

        bodyView.setTypeface(Typeface.createFromAsset(getResources().getAssets(), "Rosario-Regular.ttf"));

        if (getArguments() != null) {
            mRootView.setAlpha(0);
            mRootView.setVisibility(View.VISIBLE);
            mRootView.animate().alpha(1);
            titleView.setText(getArguments().getString(ARG_TITLE));

            // If date is before 1902, just show the string
            bylineView.setText(Html.fromHtml(" by <font color='#ffffff'>" + getArguments().getString(ARG_AUTHORS) + "</font>"));


            bodyView.setText(Html.fromHtml(getArguments().getString(ARG_BODY).replaceAll("(\r\n|\n)", "<br />")));

            Glide.with(getContext() /* context */)
                    .load(getArguments().getString(ARG_IMAGE_URL))
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
                            mMutedColor = palette.getDarkMutedColor(defaultColor);
                            mRootView.findViewById(R.id.meta_bar)
                                    .setBackgroundColor(mMutedColor);
                            return false;
                        }
                    })
                    .into(mPhotoView);
        } else {
            mRootView.setVisibility(View.GONE);
            titleView.setText("N/A");
            bylineView.setText("N/A");
            bodyView.setText("N/A");
        }
    }
}
