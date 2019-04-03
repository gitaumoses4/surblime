package com.mg.surblime.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.mg.surblime.R;

public class FormFieldLayout extends LinearLayout {

    private static final int defaultPadding = 40;
    private Paint borderPaint, labelPaint, errorPaint;
    private String label;

    private int errorColor;
    private static final int labelTextSize = 14;
    private static final int errorTextSize = 10;

    private boolean borders[] = new boolean[]{true, false, true, true};
    private String error;
    private int labelTextColor;

    public FormFieldLayout(Context context) {
        super(context);
    }

    public FormFieldLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FormFieldLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public FormFieldLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    @Override
    public boolean willNotDraw() {
        return false;
    }

    public void init(AttributeSet attributeSet) {
        setGravity(Gravity.BOTTOM);
        TypedArray typedArray = getContext().obtainStyledAttributes(attributeSet, R.styleable.FormFieldLayout);

        int paddingVertical = typedArray.getDimensionPixelSize(R.styleable.FormFieldLayout_verticalPadding, defaultPadding);
        int paddingHorizontal = typedArray.getDimensionPixelSize(R.styleable.FormFieldLayout_horizontalPadding, defaultPadding);
        int borderColor = typedArray.getColor(R.styleable.FormFieldLayout_borderColor, getResources().getColor(R.color.md_grey_600));
        labelTextColor = typedArray.getColor(R.styleable.FormFieldLayout_labelTextColor, getResources().getColor(R.color.md_grey_900));


        label = typedArray.getString(R.styleable.FormFieldLayout_label);
        error = typedArray.getString(R.styleable.FormFieldLayout_error);

        int borderAttrs[] = new int[]{R.styleable.FormFieldLayout_borderTop, R.styleable.FormFieldLayout_borderRight,
            R.styleable.FormFieldLayout_borderBottom, R.styleable.FormFieldLayout_borderLeft};

        for (int i = 0; i < borders.length; i++) {
            borders[i] = typedArray.getBoolean(borderAttrs[i], borders[i]);
        }
        setPadding(paddingHorizontal, paddingVertical, paddingHorizontal, paddingVertical);

        typedArray.recycle();

        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setColor(borderColor);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(1f);

        labelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        labelPaint.setColor(labelTextColor);
        labelPaint.setFakeBoldText(true);
        labelPaint.setTextSize(labelTextSize * getResources().getDisplayMetrics().density);
        labelPaint.setStyle(Paint.Style.FILL);

        errorPaint = new Paint(labelPaint);
        errorPaint.setColor(errorColor = getResources().getColor(R.color.md_red_500));
        errorPaint.setTextSize(errorTextSize * getResources().getDisplayMetrics().density);
    }

    public void setError(String error) {
        this.error = error;
        invalidate();
    }

    public boolean hasError() {
        return !(error == null || error.trim().isEmpty());
    }

    private void drawError(Canvas canvas) {
        TextPaint textPaint = new TextPaint(errorPaint);
        StaticLayout errorLayout = new StaticLayout(error, textPaint, canvas.getWidth(),
            Layout.Alignment.ALIGN_NORMAL, 1.0f, 1.0f,
            false);

        canvas.save();
        float x = getPaddingLeft();
        float y = getPaddingTop() + errorPaint.getTextSize() + 30;

        canvas.translate(x, y);
        errorLayout.draw(canvas);

        canvas.restore();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (borders[0])
            canvas.drawLine(0, 0, getMeasuredWidth(), 0, borderPaint);
        if (borders[1])
            canvas.drawLine(getMeasuredWidth(), 0, getMeasuredWidth(), getMeasuredHeight(), borderPaint);
        if (borders[2])
            canvas.drawLine(0, getMeasuredHeight(), getMeasuredWidth(), getMeasuredHeight(), borderPaint);
        if (borders[3])
            canvas.drawLine(0, 0, 0, getMeasuredHeight(), borderPaint);

        if (hasError()) {
            labelPaint.setColor(errorColor);
            drawError(canvas);
        } else {
            labelPaint.setColor(labelTextColor);
        }
        canvas.drawText(label, getPaddingLeft(), getPaddingTop() + labelPaint.getTextSize(), labelPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int padding = 100;
        setMeasuredDimension(this.getMeasuredWidth(), this.getMeasuredHeight() + padding);
    }
}
