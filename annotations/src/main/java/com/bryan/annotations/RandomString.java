package com.bryan.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import static java.lang.annotation.RetentionPolicy.CLASS;
import static java.lang.annotation.ElementType.FIELD;

@Retention(CLASS)
@Target(value = FIELD)
public @interface RandomString {
}