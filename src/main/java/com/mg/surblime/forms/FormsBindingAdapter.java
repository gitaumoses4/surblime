package com.mg.surblime.forms;

import androidx.databinding.BindingAdapter;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.mg.surblime.R;
import com.mg.surblime.forms.fields.FormField;
import com.mg.surblime.forms.fields.ImageField;
import com.mg.surblime.forms.fields.SpinnerField;
import com.mg.surblime.widgets.FormFieldLayout;
import com.mg.surblime.widgets.ImageInputField;
import com.mg.surblime.widgets.SurblimeLoadingButton;
import com.mg.surblime.widgets.SurblimeRadioButton;

/**
 * Created by moses on 2/22/19.
 */

public class FormsBindingAdapter {

    @BindingAdapter("onClick")
    public void setLoadingButtonClick(SurblimeLoadingButton loadingButtonClick, View.OnClickListener onClickListener) {
        loadingButtonClick.findViewById(R.id.button)
            .setOnClickListener(onClickListener);
    }

    @BindingAdapter("form:error")
    public static <S extends FormField<T, ?>, T> void setError(final TextView textView, final S formField) {
        if (formField == null) {
            return;
        }
        formField.addFormFieldListener(new FormField.FormFieldListener<T>() {
            @Override
            public void onChange(T value) {

            }

            @Override
            public void onError(String error) {
                ViewParent viewParent = textView.getParent();
                if (viewParent instanceof ViewGroup) {
                    TransitionManager.beginDelayedTransition((ViewGroup) viewParent);
                }
                if (error == null) {
                    textView.setVisibility(View.INVISIBLE);
                } else {
                    textView.setTextColor(textView.getResources().getColor(R.color.md_red_500));
                    textView.setText(error);
                    textView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @BindingAdapter("form:formField")
    public static <S extends FormField<T, ?>, T> void setRadioButtons(final RadioGroup radioButtons, final S formField) {
        if (formField == null) {
            return;
        }
        for (int i = 0; i < radioButtons.getChildCount(); i++) {
            View view = radioButtons.getChildAt(i);
            if (view instanceof SurblimeRadioButton) {
                SurblimeRadioButton radioButton = (SurblimeRadioButton) view;
                radioButton.setChecked(radioButton.getValue().equals(formField.getRawValue()));
                formField.addFormFieldListener(new FormField.FormFieldListener<T>() {
                    @Override
                    public void onChange(T value) {
                        if (radioButton.getValue().equals(formField.getRawValue()) && !radioButton.isChecked()) {
                            radioButton.setChecked(true);
                        }
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
                radioButton.setOnCheckedChangeListener((compoundButton, b) -> {
                    if (b) {
                        formField.setRawValue(radioButton.getValue());
                    }
                });
            }
        }
    }

    @BindingAdapter("form:formField")
    public static <S extends FormField<T, ?>, T> void setTextField(final TextInputLayout textField, final S formField) {
        final EditText editText = textField.getEditText();
        if (editText != null && formField != null) {
            editText.setText(formField.getRawValue());
            formField.addFormFieldListener(new FormField.FormFieldListener<T>() {
                @Override
                public void onChange(T value) {
                    if (!editText.getText().toString().equals(formField.getRawValue())) {
                        editText.setText(formField.getRawValue());
                    }
                }

                @Override
                public void onError(String error) {
                    textField.setError(formField.getError());
                }
            });

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    formField.setRawValue(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }

    @BindingAdapter("form:formField")
    @SuppressWarnings("unchecked")
    public static <S extends FormField<T, ?>, T> void setSpinner(final Spinner spinner, final S field) {
        if (field == null) {
            return;
        }
        if (field instanceof SpinnerField) {
            SpinnerField<T> formField = (SpinnerField) field;
            spinner.setAdapter(formField.getAdapter(spinner.getContext()));
            spinner.setSelection(formField.getOptionIndex(formField.get()));

            formField.addFormFieldListener(new FormField.FormFieldListener<T>() {
                @Override
                public void onChange(T value) {
                    if (!spinner.getSelectedItem().equals(value)) {
                        spinner.setSelection(formField.getOptionIndex(value), true);
                    }
                }

                @Override
                public void onError(String error) {

                }
            });

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    formField.set(formField.getOptionAt(i));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }

    @BindingAdapter("form:formField")
    public static <S extends FormField<Boolean, ?>> void setCheckbox(final CheckBox checkbox, final S field) {
        if (field == null) {
            return;
        }
        if (checkbox != null) {
            checkbox.setChecked(field.get());
            field.addFormFieldListener(new FormField.FormFieldListener<Boolean>() {
                @Override
                public void onChange(Boolean value) {
                    if (checkbox.isChecked() != value) {
                        checkbox.setChecked(value);
                    }
                }

                @Override
                public void onError(String error) {

                }
            });
            checkbox.setOnCheckedChangeListener((compoundButton, b) -> {
                field.set(b);
            });
        }
    }

    /*
    ============================================================================================================
     */
    @BindingAdapter("form:formField")
    public static <S extends FormField<T, ?>, T> void setFormField(FormFieldLayout formField, final S field) {
        if (field != null) {
            View view = formField.getChildAt(0);
            if (view != null && !field.getFormContext().bindField(field.key, field, view, formField)) {
                if (view instanceof EditText) {
                    FormsBindingAdapterBuilder.buildEditText(formField, view, field);
                } else if (view instanceof Spinner) {
                    FormsBindingAdapterBuilder.buildSpinner(formField, view, (SpinnerField<?>) field);
                }
            }

            if (formField instanceof ImageInputField) {
                ImageField imageField = (ImageField) field;
                imageField.setImageInputField((ImageInputField) formField);
                FormsBindingAdapterBuilder.buildImageView((ImageInputField) formField, imageField);
            }
        }
    }
}
