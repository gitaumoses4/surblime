package com.mg.surblime.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.os.Build;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.mg.surblime.R;

/**
 * Created by moses on 2/24/19.
 */

public class SurblimeLoadingButton extends FrameLayout {

    private int background;
    private String text;
    private boolean loading;
    private boolean disabled;
    private int textColor;
    private int progressBarColor;

    private ProgressBar progressBar;
    private Button button;

    public SurblimeLoadingButton(Context context) {
        super(context);
    }

    public SurblimeLoadingButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setAttrs(context, attrs);
    }

    public SurblimeLoadingButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttrs(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SurblimeLoadingButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setAttrs(context, attrs);
    }

    private void setAttrs(Context context, AttributeSet attrs) {
        LayoutInflater.from(context)
            .inflate(R.layout.button_loading, this, true);
        progressBar = findViewById(R.id.progressBar);
        button = findViewById(R.id.button);

        button.setOnClickListener(view -> this.callOnClick());

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SurblimeLoadingButton);

        background = typedArray.getColor(R.styleable.SurblimeLoadingButton_buttonBackground,
            getResources().getColor(R.color.md_white_1000));
        text = typedArray.getString(R.styleable.SurblimeLoadingButton_text);
        disabled = typedArray.getBoolean(R.styleable.SurblimeLoadingButton_disabled, false);
        loading = typedArray.getBoolean(R.styleable.SurblimeLoadingButton_loading, false);
        progressBarColor = typedArray.getColor(R.styleable.SurblimeLoadingButton_progressBarColor,
            getResources().getColor(R.color.md_green_500));
        textColor = typedArray.getColor(R.styleable.SurblimeLoadingButton_textColor,
            getResources().getColor(R.color.md_black_1000));


        button.setBackgroundColor(background);
        setBackgroundColor(background);
        button.setText(text);
        if (typedArray.hasValue(R.styleable.SurblimeLoadingButton_textColor)) {
            button.setTextColor(textColor);
        }

        if (typedArray.hasValue(R.styleable.SurblimeLoadingButton_progressBarColor)) {
            progressBar.getIndeterminateDrawable()
                .setColorFilter(
                    progressBarColor,
                    PorterDuff.Mode.MULTIPLY
                );
        }
        button.setEnabled(!disabled);
        checkState();
        typedArray.recycle();
    }

    private void checkState() {
        TransitionManager.beginDelayedTransition(this);
        button.setVisibility(loading ? View.INVISIBLE : View.VISIBLE);
        this.progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        button.setEnabled(!(disabled || loading));
        if (!button.isEnabled()) {
            this.getBackground().setColorFilter(getResources().getColor(R.color.md_grey_500)
                , PorterDuff.Mode.LIGHTEN
            );
        } else {
            this.setBackgroundColor(background);
        }
    }

    public void setLoading(boolean loading) {
        this.loading = loading;
        checkState();
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
        this.checkState();
    }
}
