package com.mg.surblime.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.mg.surblime.R;
import com.mg.surblime.util.Tools;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddNewImage extends CircleImageView {

    private RectF mDrawableRect;
    private Bitmap addImageBitmap;

    private Paint imagePaint;

    public AddNewImage(Context context) {
        super(context);
        init();
    }

    public AddNewImage(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AddNewImage(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public void init() {
        mDrawableRect = new RectF();

        addImageBitmap = Tools.drawableToBitmap(getResources().getDrawable(R.drawable.add_image));

        imagePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    private RectF calculateBounds() {
        int availableWidth = getWidth() - getPaddingLeft() - getPaddingRight();
        int availableHeight = getHeight() - getPaddingTop() - getPaddingBottom();

        int sideLength = Math.min(availableWidth, availableHeight);

        float left = getPaddingLeft() + (availableWidth - sideLength) / 2f;
        float top = getPaddingTop() + (availableHeight - sideLength) / 2f;

        return new RectF(left, top, left + sideLength, top + sideLength);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        mDrawableRect.set(calculateBounds());
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setImageDrawable(getResources().getDrawable(R.color.md_grey_300));
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        float imageX = mDrawableRect.centerX();
        float imageY = mDrawableRect.centerY();

        canvas.drawBitmap(this.addImageBitmap, imageX - (addImageBitmap.getWidth() >> 1), imageY - (addImageBitmap.getHeight() >> 1), imagePaint);
    }
}
