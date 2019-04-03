package com.mg.surblime.util;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by Moses Gitau on 4/4/18 at 12:35 AM.
 */

public class SpaceItemDecorator extends RecyclerView.ItemDecoration {

    private final int left;
    private final int top;
    private final int right;
    private final int bottom;

    public SpaceItemDecorator(int left, int top, int right, int bottom) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = left;
        outRect.top = top;
        outRect.right = right;
        outRect.bottom = bottom;
        super.getItemOffsets(outRect, view, parent, state);
    }
}
