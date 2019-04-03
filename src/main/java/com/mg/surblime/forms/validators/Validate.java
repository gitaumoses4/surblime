package com.mg.surblime.forms.validators;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by moses on 2/22/19.
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface Validate {
    String value();
}
