package com.mg.surblime.forms;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by moses on 2/22/19.
 */

@Retention(RetentionPolicy.RUNTIME)
public @interface Field {
    String value();

    String type() default "text";

    boolean required() default true;

    String[] options() default {};

    // only for images
    boolean multiple() default true;
}
