package com.mg.surblime.forms.fields;

import android.view.View;

import com.mg.surblime.forms.FormContext;
import com.mg.surblime.forms.validators.BaseValidator;
import com.mg.surblime.widgets.FormFieldLayout;

import java.util.ArrayList;

/**
 * Created by moses on 2/22/19.
 */

public abstract class FormField<T, Q extends BaseValidator<?, ?, ?>> {

    public static final String TEXT = "text";
    public static final String NUMBER = "number";
    public static final String DOUBLE = "double";
    public static final String CHECKBOX = "checkbox";
    public static final String RADIO = "radio";
    public static final String IMAGE = "image";
    public static final String SPINNER = "spinner";


    private String error;
    public String key;
    private T value;
    public Q validator;
    private FormContext<?, ?> formContext;
    public boolean required = true;

    private final ArrayList<FormFieldListener<T>> listeners = new ArrayList<>();


    public FormField(T value) {
        this.value = value;
        this.validator = getValidator();
    }

    public void setFormContext(FormContext<?, ?> formContext) {
        this.formContext = formContext;
    }

    public FormContext<?, ?> getFormContext() {
        return formContext;
    }

    public T get() {
        return value;
    }

    public void set(T t) {
        this.value = t;
        this.notifyValueChange();
    }

    public void notifyValueChange() {
        for (FormFieldListener<T> listener : listeners) {
            listener.onChange(value);
        }
    }

    public void notifyError() {
        for (FormFieldListener<T> listener : listeners) {
            listener.onError(error);
        }
    }

    public void addFormFieldListener(FormFieldListener<T> listener) {
        listeners.add(listener);
    }

    public void removeFormFieldListener(FormFieldListener<T> listener) {
        listeners.remove(listener);
    }

    public void setError(String errorMessage) {
        this.error = errorMessage;
        this.notifyError();
    }

    public void clearError() {
        this.error = null;
    }

    public String getError() {
        return error;
    }

    public boolean hasError() {
        return error != null && !error.isEmpty();
    }

    public interface FormFieldListener<T> {
        void onChange(T value);

        void onError(String error);
    }

    public abstract void setRawValue(String string);

    public abstract String getRawValue();

    public void setValidator(Q q) {
        this.validator = q;
    }

    public abstract Q getValidator();

    public <R extends View> R bind(R r, FormFieldLayout formFieldLayout, OnChangeListener<T> onChangeListener) {
        onChangeListener.onChange(get());
        this.addFormFieldListener(new FormFieldListener<T>() {
            @Override
            public void onChange(T value) {
                if (get() != value)
                    onChangeListener.onChange(value);
            }

            @Override
            public void onError(String error) {
                formFieldLayout.setError(error);
            }
        });
        return r;
    }

    public interface OnChangeListener<T> {
        void onChange(T t);
    }
}
