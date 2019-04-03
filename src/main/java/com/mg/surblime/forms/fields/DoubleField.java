package com.mg.surblime.forms.fields;

import com.mg.surblime.forms.validators.DoubleValidator;

/**
 * Created by moses on 2/22/19.
 */

public class DoubleField extends FormField<Double, DoubleValidator> {
    public DoubleField(Double value) {
        super(value);
    }

    public DoubleField() {
        super(null);
    }

    @Override
    public void setRawValue(String string) {
        this.set(Double.parseDouble(string));
    }

    @Override
    public String getRawValue() {
        return String.valueOf(this.get());
    }

    @Override
    public DoubleValidator getValidator() {
        return new DoubleValidator();
    }
}
