package com.mg.surblime.forms.fields;

import com.mg.surblime.forms.validators.NumberValidator;

/**
 * Created by moses on 2/22/19.
 */

public class NumberField extends FormField<Long, NumberValidator> {

    public NumberField(Long value) {
        super(value);
    }

    public NumberField() {
        super(null);
    }

    @Override
    public void setRawValue(String string) {
        this.set(Long.parseLong(string));
    }

    @Override
    public String getRawValue() {
        return String.valueOf(this.get());
    }

    @Override
    public NumberValidator getValidator() {
        return new NumberValidator();
    }
}
