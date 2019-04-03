package com.mg.surblime.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.appcompat.widget.AppCompatRadioButton;
import android.util.AttributeSet;

import com.mg.surblime.R;

/**
 * Created by moses on 2/22/19.
 */

public class SurblimeRadioButton extends AppCompatRadioButton {
    private String value;

    public SurblimeRadioButton(Context context) {
        super(context);
    }

    public SurblimeRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setAttrs(context, attrs);
    }

    public SurblimeRadioButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttrs(context, attrs);
    }

    private void setAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SurblimeRadioButton);

        value = typedArray.getString(R.styleable.SurblimeRadioButton_fieldValue);
        typedArray.recycle();
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
