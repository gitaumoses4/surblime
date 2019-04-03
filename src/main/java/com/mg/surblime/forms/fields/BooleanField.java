package com.mg.surblime.forms.fields;

import com.mg.surblime.forms.validators.BooleanValidator;

/**
 * Created by moses on 2/23/19.
 */

public class BooleanField extends FormField<Boolean, BooleanValidator> {
    public BooleanField(Boolean value) {
        super(value);
    }

    public BooleanField() {
        super(null);
    }

    @Override
    public void setRawValue(String string) {
        this.set(Boolean.parseBoolean(string));
    }

    @Override
    public String getRawValue() {
        return String.valueOf(get());
    }

    @Override
    public BooleanValidator getValidator() {
        return new BooleanValidator();
    }
}
