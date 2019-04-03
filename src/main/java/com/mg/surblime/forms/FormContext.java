package com.mg.surblime.forms;

import androidx.databinding.Bindable;
import android.util.Log;
import android.view.View;

import com.mg.surblime.BR;
import com.mg.surblime.BaseModel;
import com.mg.surblime.ObservableViewModel;
import com.mg.surblime.forms.fields.BooleanField;
import com.mg.surblime.forms.fields.DoubleField;
import com.mg.surblime.forms.fields.FormField;
import com.mg.surblime.forms.fields.ImageField;
import com.mg.surblime.forms.fields.NumberField;
import com.mg.surblime.forms.fields.SpinnerField;
import com.mg.surblime.forms.fields.TextField;
import com.mg.surblime.forms.validators.BaseValidator;
import com.mg.surblime.widgets.FormFieldLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by moses on 2/21/19.
 */

public abstract class FormContext<Q extends FormContext<?, ?>, S extends FormResponse> extends ObservableViewModel<BaseModel> {

    private final Class<S> responseDataClass;
    public transient Q self;
    public transient Class<Q> selfClass;

    @Bindable
    public transient S responseData;

    public static final int PENDING = 4;

    @Bindable
    public transient boolean hasErrors;

    public final HashMap<String, Object> defaults = new HashMap<>();
    public final HashMap<String, FormField> fields = new HashMap<>();
    public final HashMap<String, BaseValidator> validators = new HashMap<>();

    private transient ArrayList<FormListener<Q>> formListeners = new ArrayList<>();

    @Bindable
    public transient String errorMessage = null;

    @SuppressWarnings("unchecked")
    public void setField(String fieldName, Object value) {
        FormField field = fields.get(fieldName);
        if (field != null)
            field.set(value);
    }

    public void setError(String fieldName, String error) {
        FormField field = fields.get(fieldName);
        if (field != null)
            field.setError(error);
    }

    public void setResponseData(S s) {
        this.responseData = s;
        notifyPropertyChanged(BR.responseData);
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
        notifyPropertyChanged(BR.hasErrors);
    }

    public FormField<?, ?> getField(String name) {
        return fields.get(name);
    }

    @SuppressWarnings("unchecked")
    public FormContext(Class<Q> selfClass, Class<S> responseDataClass) {
        this.status = PENDING;
        this.responseDataClass = responseDataClass;
        try {
            this.selfClass = selfClass;
            this.responseData = responseDataClass.newInstance();
            this.self = selfClass.cast(this);

            this.buildDefaults(defaults);
            for (java.lang.reflect.Field fi : selfClass.getFields()) {
                Field field = fi.getAnnotation(Field.class);
                if (field != null) {
                    String fieldName = field.value();
                    String type = field.type();
                    boolean required = field.required();
                    String[] options = field.options();
                    FormField formField = createField(type, fi.getType());
                    if (formField != null) {
                        formField.setFormContext(this);
                        if (formField instanceof SpinnerField && options.length > 0) {
                            ((SpinnerField<String>) formField).setOptions(options);
                        }
                        fields.put(fieldName, formField);
                        formField.key = fieldName;
                        formField.required = required;
                        if (formField.required) {
                            formField.validator.notEmpty();
                        }

                        if (formField instanceof ImageField && !field.multiple()) {
                            ((ImageField) formField).setSingle(true);
                        }
                        Object value = defaults.get(fieldName);
                        formField.set(value);
                        formField.addFormFieldListener(new FormField.FormFieldListener() {
                            @Override
                            public void onChange(Object value) {
                                validate(fieldName);
                            }

                            @Override
                            public void onError(String error) {

                            }
                        });
                    }
                }
            }
            this.configureFields(fields);
            this.buildValidators(validators);
            this.checkForErrors();

        } catch (Exception ignored) {
            Log.e("FormContext", "Creating fields", ignored);
        }
    }

    public abstract void buildValidators(HashMap<String, BaseValidator> validators);

    public abstract void buildDefaults(HashMap<String, Object> defaults);

    private Class<?> getBoxedClass(Class<?> classOfType) {
        if (classOfType.isPrimitive()) {
            if (classOfType.isAssignableFrom(boolean.class)) {
                return Boolean.class;
            } else if (classOfType.isAssignableFrom(byte.class)) {
                return Byte.class;
            } else if (classOfType.isAssignableFrom(char.class)) {
                return Character.class;
            } else if (classOfType.isAssignableFrom(float.class)) {
                return Float.class;
            } else if (classOfType.isAssignableFrom(int.class)) {
                return Integer.class;
            } else if (classOfType.isAssignableFrom(long.class)) {
                return Long.class;
            } else if (classOfType.isAssignableFrom(short.class)) {
                return Short.class;
            } else if (classOfType.isAssignableFrom(double.class)) {
                return Double.class;
            } else {
                return classOfType;
            }
        } else {
            return classOfType;
        }
    }

    @SuppressWarnings("unchecked")
    private FormField createField(String type, Class<?> classOfType) {
        classOfType = getBoxedClass(classOfType);
        switch (type) {
            case FormField.DOUBLE:
                return new DoubleField();
            case FormField.NUMBER:
                return new NumberField();
            case FormField.TEXT:
                if (classOfType.isAssignableFrom(Boolean.class)) {
                    throw new RuntimeException("Please specify a Checkbox or Radio button for a boolean field");
                }
                return new TextField();
            case FormField.CHECKBOX:
            case FormField.RADIO:
                return classOfType.isAssignableFrom(Boolean.class)
                    ? new BooleanField() :
                    new TextField();
            case FormField.SPINNER:
                return new SpinnerField<>();
            case FormField.IMAGE:
                return new ImageField();
        }
        return null;
    }

    public ArrayList<String> keys() {
        return new ArrayList<>(fields.keySet());
    }

    @SuppressWarnings("unchecked")
    public void validate(String fieldName) {
        isValid(fieldName, true);
        this.errorMessage = null;
        notifyPropertyChanged(BR.errorMessage);
        this.checkForErrors();
    }

    public boolean isValid(String fieldName, boolean showError) {
        FormField formField = fields.get(fieldName);
        BaseValidator validator = validators.get(fieldName);


        validator = validator == null ? formField.validator : validator;
        if (validator != null) {
            if (formField.required) {
                validator.notEmpty();
            }
            return validator.isValid(formField, showError);
        }
        return true;
    }


    public void checkForErrors() {
        boolean errors = false;
        for (Map.Entry<String, FormField> field : fields.entrySet()) {
            if (field.getValue().required) {
                if (field.getValue().hasError() || !isValid(field.getKey(), false)) {
                    errors = true;
                    break;
                }
            }
        }
        this.hasErrors = errors;
        notifyPropertyChanged(BR.hasErrors);
    }

    public <V extends View> boolean bindField(String name, FormField<?, ?> field, V view, FormFieldLayout fieldLayout) {
        return false;
    }

    public void pending() {
        this.status = PENDING;
        this.notifyPropertyChanged(BR.status);
    }

    public void submit(View view) {
        for (FormListener<Q> formListener : formListeners) {
            formListener.onSubmit(view, self);
        }
    }

    public void addFormListener(FormListener<Q> formListener) {
        this.formListeners.add(formListener);
    }

    public void removeFormListener(FormListener<Q> formListener) {
        this.formListeners.remove(formListener);
    }

    public interface FormListener<T extends FormContext<?, ?>> {
        void onSubmit(View view, T t);
    }

    public void configureFields(HashMap<String, FormField> fields) {

    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        notifyPropertyChanged(BR.errorMessage);
    }
}
