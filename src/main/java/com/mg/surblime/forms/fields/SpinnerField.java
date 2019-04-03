package com.mg.surblime.forms.fields;

import android.content.Context;
import androidx.annotation.LayoutRes;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.mg.surblime.forms.validators.TextValidator;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by moses on 2/22/19.
 */

public class SpinnerField<T> extends FormField<T, TextValidator> {

    public ArrayList<T> options = new ArrayList<>();
    private Spinner spinner;
    private @LayoutRes
    int layoutRes = android.R.layout.simple_spinner_dropdown_item;

    public SpinnerField(T value) {
        super(value);
    }

    public void setOptions(T[] options) {
        this.options.clear();
        this.options.addAll(Arrays.asList(options));
        spinner.setAdapter(getAdapter(spinner.getContext()));
    }

    public void setOptions(ArrayList<T> options) {
        this.options.clear();
        this.options.addAll(options);
        spinner.setAdapter(getAdapter(spinner.getContext()));
    }

    public SpinnerField() {
        super(null);
    }

    public void setSpinner(Spinner spinner) {
        this.spinner = spinner;
    }

    public Spinner getSpinner() {
        return spinner;
    }

    public void setLayoutRes(@LayoutRes int layoutRes) {
        this.layoutRes = layoutRes;
    }

    public ArrayAdapter<T> getAdapter(Context context) {
        return new ArrayAdapter<>(context, layoutRes, options);
    }

    public ArrayList<T> getOptions() {
        return options;
    }

    public T getOptionAt(int i) {
        return options.get(i);
    }

    public int getOptionIndex(T t) {
        return t == null ? 0 : options.indexOf(t);
    }

    @Override
    public void setRawValue(String string) {

    }

    @Override
    public String getRawValue() {
        return get() == null ? null : get().toString();
    }

    @Override
    public TextValidator getValidator() {
        return new TextValidator() {
            @Override
            public String getValue() {
                return String.valueOf(super.getValue());
            }
        };
    }
}
