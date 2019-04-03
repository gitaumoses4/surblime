package com.mg.surblime.forms.fields;

import com.mg.surblime.forms.validators.TextValidator;

import org.w3c.dom.Text;

/**
 * Created by moses on 2/22/19.
 */

public class TextField extends FormField<String, TextValidator> {

    public TextField(String value) {
        super(value);
    }

    public TextField() {
        super("");
    }

    @Override
    public void setRawValue(String string) {
        this.set(string);
    }

    @Override
    public String getRawValue() {
        return this.get();
    }

    @Override
    public TextValidator getValidator() {
        return new TextValidator();
    }
}
