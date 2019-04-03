package com.mg.surblime.activities;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.liuguangqiang.swipeback.SwipeBackLayout;
import com.mg.surblime.R;

/**
 * Created by moses on 5/2/18.
 */

public abstract class SwipeBackActivity extends ToolbarActivity implements SwipeBackLayout.SwipeBackListener {

    private SwipeBackLayout swipeBackLayout;
    private ImageView ivShadow;

    public void setDragEdge(SwipeBackLayout.DragEdge dragEdge) {
        swipeBackLayout.setDragEdge(dragEdge);
    }

    public SwipeBackLayout getSwipeBackLayout() {
        return swipeBackLayout;
    }

    @Override
    public void onViewPositionChanged(float fractionAnchor, float fractionScreen) {
        ivShadow.setAlpha(1 - fractionScreen);
    }

    public abstract View getContentView();

    @Override
    public void setContentView() {
        View view = getContentView();
        RelativeLayout container = new RelativeLayout(this);
        swipeBackLayout = new SwipeBackLayout(this);
        swipeBackLayout.addView(view, new SwipeBackLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        swipeBackLayout.setOnSwipeBackListener(this);
        ivShadow = new ImageView(this);

        ivShadow.setBackgroundColor(getResources().getColor(R.color.md_grey_900));
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        container.addView(ivShadow, params);
        container.addView(swipeBackLayout);
        setContentView(container);
    }
}
