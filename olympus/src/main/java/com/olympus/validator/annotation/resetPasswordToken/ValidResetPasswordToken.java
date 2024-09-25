package com.olympus.validator.annotation.resetPasswordToken;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Documented
@Constraint(validatedBy = ValidResetPasswordTokenValidator.class)
@Target({FIELD, METHOD, PARAMETER, TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidResetPasswordToken {
    String message() default "Token is invalid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
