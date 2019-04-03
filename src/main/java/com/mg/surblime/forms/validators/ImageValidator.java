package com.mg.surblime.forms.validators;

import com.mg.surblime.forms.fields.ImageField;

import java.util.ArrayList;

public class ImageValidator extends BaseValidator<ImageField, ArrayList<String>, ImageValidator> {
    public ImageValidator() {
        super(ImageValidator.class);
    }

    @Override
    public Evaluator<ArrayList<String>> getNotEmptyEvaluator() {
        return value -> value == null || value.isEmpty();
    }
}
