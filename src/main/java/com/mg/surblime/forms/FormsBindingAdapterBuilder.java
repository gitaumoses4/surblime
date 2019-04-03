package com.mg.surblime.forms;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.mg.surblime.forms.fields.FormField;
import com.mg.surblime.forms.fields.ImageField;
import com.mg.surblime.forms.fields.SpinnerField;
import com.mg.surblime.util.CloudinaryImage;
import com.mg.surblime.widgets.FormFieldLayout;
import com.mg.surblime.widgets.ImageInputField;

public class FormsBindingAdapterBuilder {

    public static <S extends FormField<T, ?>, T> void buildEditText(FormFieldLayout formFieldLayout, View editTextView, S field) {
        EditText editText = (EditText) editTextView;
        field.bind(editText, formFieldLayout, (t -> editText.setText(field.getRawValue())))
            .addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    field.setRawValue(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
    }

    public static <T> void buildSpinner(FormFieldLayout formFieldLayout, View spinnerView, SpinnerField<T> field) {
        Spinner spinner = (Spinner) spinnerView;
        spinner.setAdapter(field.getAdapter(spinnerView.getContext()));
        field.setSpinner(spinner);
        field.bind(spinner, formFieldLayout, (t -> spinner.setSelection(field.getOptionIndex(t))))
            .setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        field.set(field.getOptionAt(i));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                }
            );
    }

    public static void buildImageView(ImageInputField imageInputField, ImageField imageField) {
        imageField.bind(imageInputField, imageInputField, (strings -> {
        })).setImageChangeListener(new ImageInputField.ImageChangeListener() {
            @Override
            public void imageAdded(CloudinaryImage cloudinaryImage) {
                imageField.add(cloudinaryImage.url);
            }

            @Override
            public void imageRemoved(CloudinaryImage cloudinaryImage) {
                imageField.remove(cloudinaryImage.url);
            }
        });
    }
}
