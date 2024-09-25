package com.olympus.validator.annotation.user;

import com.olympus.service.IUserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Validator cua Annotation @UniqueEmail
 */
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {
    private final IUserService userService;

    @Autowired
    public UniqueEmailValidator(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        try {
            return email != null && !userService.existsEmail(email);
        } catch (Exception ex) {
            return false;
        }
    }
}
