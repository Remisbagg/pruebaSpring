package com.back.validator.annotation.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Documented
@Constraint(validatedBy = ExistUserIdValidator.class)
@Target({FIELD, METHOD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistUserById {
    String message() default "The Id does not exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
