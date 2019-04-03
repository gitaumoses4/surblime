package com.mg.surblime.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.StyleRes;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.mg.surblime.R;
import com.mg.surblime.util.Tools;

/**
 * Created by Moses Gitau on 3/25/18 at 11:01 AM.
 */

public abstract class ToolbarActivity extends SurblimeActivity {

    private Toolbar toolbar;


    protected void setContentScrim(CollapsingToolbarLayout collapsingToolbarLayout, ImageView collapsingImageView) {
        Bitmap contentScrim = null;
        try {
            Drawable drawable = collapsingImageView.getDrawable();
            contentScrim = Tools.BlurBuilder.blur(ToolbarActivity.this, Tools.drawableToBitmap(drawable));
            Drawable result = new BitmapDrawable(getResources(), contentScrim);
            collapsingToolbarLayout.setContentScrim(result);
            collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(android.R.color.transparent));
        } catch (Exception ignore) {

        }
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    protected void setContentScrim(int collasingToolbarId, int collapsingImageId) {
        setContentScrim((CollapsingToolbarLayout) findViewById(collasingToolbarId), (ImageView) findViewById(collapsingImageId));
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView();
        toolbar = findViewById(toolbarId());
        setSupportActionBar(toolbar);
    }

    public abstract void setContentView();

    public abstract @IdRes
    int toolbarId();

    @Override
    public void setFullscreen(final boolean fullscreen) {
        super.setFullscreen(fullscreen);
        Animation animation = AnimationUtils.loadAnimation(this, fullscreen ? R.anim.toolbar_exit_animation : R.anim.toolbar_enter_animation);
        animation.setDuration(300);
        animation.setFillAfter(true);
        animation.setFillBefore(true);
        toolbar.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                toolbar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (fullscreen) {
                    toolbar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }

    public void setDisplayHomeAsUpEnabled(boolean showHomeAsUp) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(showHomeAsUp);
            if (showHomeAsUp) {
                toolbar.setNavigationOnClickListener(view -> onBackPressed());
            }
        }
    }

    @SuppressLint("PrivateResource")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void circleReveal(int viewID, int posFromRight, int posFromTop, boolean containsOverflow, final boolean isShow) {
        final View myView = findViewById(viewID);

        int width = myView.getWidth();

        if (posFromRight > 0)
            width -= (posFromRight * getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material)) - (getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) / 2);
        if (containsOverflow)
            width -= getResources().getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material);

        int cx = width;
        int cy = 0;

        Animator animator;
        if (isShow)
            animator = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, (float) width);
        else
            animator = ViewAnimationUtils.createCircularReveal(myView, cx, cy, (float) width, 0);

        animator.setDuration((long) 200);

        // make the view invisible when the animation is done
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isShow) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.INVISIBLE);
                }
            }
        });

        // make the view visible and start the animation
        if (isShow)
            myView.setVisibility(View.VISIBLE);

        // start the animation
        animator.start();
    }

}
