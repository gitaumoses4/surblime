package com.mg.surblime.forms.validators;

import com.mg.surblime.forms.fields.NumberField;

/**
 * Created by moses on 2/22/19.
 */

public class NumberValidator extends BaseValidator<NumberField, Long, NumberValidator> {
    public NumberValidator() {
        super(NumberValidator.class);
    }
}
