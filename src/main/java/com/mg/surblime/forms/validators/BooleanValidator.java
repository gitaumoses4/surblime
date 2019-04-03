package com.mg.surblime.forms.validators;

import com.mg.surblime.forms.fields.BooleanField;

/**
 * Created by moses on 2/23/19.
 */

public class BooleanValidator extends BaseValidator<BooleanField, Boolean, BooleanValidator> {
    public BooleanValidator() {
        super(BooleanValidator.class);
    }

    public BooleanValidator isTrue(String message) {
        return this.equals(true, message);
    }

    public BooleanValidator isFalse(String message) {
        return this.equals(false, message);
    }
}
