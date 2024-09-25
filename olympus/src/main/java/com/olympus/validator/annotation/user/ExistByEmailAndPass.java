package com.olympus.validator.annotation.user;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ExistByEmailAndPassValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExistByEmailAndPass {
    String message() default "Invalid email or password";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
