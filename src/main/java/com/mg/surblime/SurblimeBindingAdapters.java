package com.mg.surblime;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import androidx.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import androidx.annotation.ColorRes;
import androidx.annotation.LayoutRes;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.core.view.ViewCompat;
import androidx.appcompat.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.mg.surblime.util.Tools;
import com.squareup.picasso.Picasso;

/**
 * Created by Moses Gitau on 3/22/18 at 6:39 PM.
 */

public class SurblimeBindingAdapters {


    @BindingAdapter("image")
    public static void setImageUri(ImageView view, String imageUri) {
        if (imageUri == null) {
            view.setImageURI(null);
        } else {
            Picasso picasso = Tools.getPicasso(view.getContext());
            picasso.load(imageUri)
                .placeholder(R.color.colorDivider)
                .into(view);
        }
    }


    @BindingAdapter("transitionName")
    public static void setTransitionName(View view, String transitionName) {
        ViewCompat.setTransitionName(view, transitionName);
    }

    @BindingAdapter("android:src")
    public static void setImageUri(ImageView view, Uri imageUri) {
        view.setImageURI(imageUri);
    }

    @BindingAdapter("circularTile")
    public static void setCircularTile(ImageView imageView, String text) {
        TextDrawable textDrawable = TextDrawable.builder().beginConfig()
            .toUpperCase().endConfig()
            .buildRound(String.valueOf(text.charAt(0)), ColorGenerator.MATERIAL.getRandomColor());

        imageView.setImageDrawable(textDrawable);
    }

    @BindingAdapter("android:src")
    public static void setImageDrawable(ImageView view, Drawable drawable) {
        view.setImageDrawable(drawable);
    }

    @BindingAdapter("android:src")
    public static void setImageResource(ImageView imageView, int resource) {
        imageView.setImageDrawable(imageView.getContext().getResources().getDrawable(resource));
    }

    @BindingAdapter("backgroundTint")
    public static void setBackgroundTint(FloatingActionButton floatingActionButton, @ColorRes int backgroundTint) {
        floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(floatingActionButton.getContext().getResources().getColor(backgroundTint)));
    }

    @SuppressLint("RestrictedApi")
    @BindingAdapter("backgroundTint")
    public static void setBackgroundTint(AppCompatTextView textView, @ColorRes int backgroundTint) {
        textView.setSupportBackgroundTintList(ColorStateList.valueOf(textView.getContext().getResources().getColor(backgroundTint)));
    }

    @BindingAdapter("android:src")
    public static void setImageDrawable(FloatingActionButton floatingActionButton, int resource) {
        floatingActionButton.setImageResource(resource);
    }

    @BindingAdapter({"layout", "refreshListener"})
    public static void setLayoutAndRefreshListener(FrameLayout frameLayout, @LayoutRes int layoutRes, final ObservableViewModel.OnRefreshListener onRefreshListener) {
        if (layoutRes != 0) {
            LayoutInflater layoutInflater = LayoutInflater.from(frameLayout.getContext());
            frameLayout.removeAllViews();
            View child = layoutInflater.inflate(layoutRes, null);
            child.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            frameLayout.addView(child);
            child.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRefreshListener.onRefresh();
                }
            });
        }
    }

    @BindingAdapter("layout")
    public static void setLayout(FrameLayout frameLayout, @LayoutRes int layoutRes) {
        if (layoutRes != 0) {
            frameLayout.removeAllViews();
            LayoutInflater layoutInflater = LayoutInflater.from(frameLayout.getContext());
            View child = layoutInflater.inflate(layoutRes, null);
            child.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            frameLayout.addView(child);
        }
    }
}
