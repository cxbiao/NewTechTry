package com.bryan.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

@Retention(CLASS)
@Target(value = FIELD)
public @interface RandomInt {
    int minValue() default 0;
    int maxValue() default 65535;
}
