package com.mg.surblime.forms.validators;

import com.mg.surblime.forms.fields.TextField;

import java.util.regex.Pattern;

/**
 * Created by moses on 2/22/19.
 */

public class TextValidator extends BaseValidator<TextField, String, TextValidator> {

    public TextValidator() {
        super(TextValidator.class);
    }

    @Override
    public Evaluator<String> getNotEmptyEvaluator() {
        return (value -> value == null || value.isEmpty());
    }

    public TextValidator minLength(final int length, String message) {
        return custom(message, (value) -> value.length() < length);
    }

    public TextValidator maxLength(final int length, String message) {
        return custom(message, value -> value.length() > length);
    }

    public TextValidator length(final int min, final int max, String message) {
        return custom(message, value -> (value.length() < min || value.length() > max));
    }

    public TextValidator matches(final String match, String message) {
        return custom(message, value -> !value.matches(match));
    }

    public TextValidator matches(final Pattern pattern, String message) {
        return custom(message, value -> !pattern.matcher(value).matches());
    }

    public TextValidator validEmail(String message) {
        return matches(
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE),
            message
        );
    }

    public TextValidator validEmail() {
        return validEmail("Please enter a valid email");
    }
}
