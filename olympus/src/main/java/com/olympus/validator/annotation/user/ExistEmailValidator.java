package com.olympus.validator.annotation.user;

import com.olympus.service.IUserService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class ExistEmailValidator implements ConstraintValidator<ExistEmail, String> {
    private final IUserService userService;

    @Autowired
    public ExistEmailValidator(IUserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        try {
            return email != null && userService.existsEmail(email);
        } catch (Exception e) {
            return false;
        }
    }
}
